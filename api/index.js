const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const cors = require('cors');

const app = express();
app.use(cors());

// 1. Search API - moontv.to
app.get('/api/search', async (req, res) => {
    const query = req.query.q;
    if (!query) return res.status(400).json({ error: "Movie name required" });

    try {
        const searchUrl = `https://moontv.to/filter?keyword=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
                'Accept': 'text/html'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        $('.movies .item').each((i, el) => {
            const title = $(el).find('.title').text().trim();
            const pageLink = $(el).attr('href');
            let poster = $(el).find('img.lazyload').attr('data-src') || $(el).find('img').attr('src');

            if (title && pageLink) {
                if(pageLink.startsWith('/')) {
                    movies.push({ title, pageLink: `https://moontv.to${pageLink}`, poster });
                } else {
                    movies.push({ title, pageLink, poster });
                }
            }
        });

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Scraping failed: " + error.message });
    }
});

// 2. Get Video Link API (Extracts the embed iframe URL from moontv.to)
app.get('/api/get-links', async (req, res) => {
    const pageUrl = req.query.url;
    if (!pageUrl) return res.status(400).json({ error: "Page URL required" });

    try {
        const { data } = await axios.get(pageUrl, {
            headers: { 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)' }
        });
        const $ = cheerio.load(data);
        
        // Find iframe source (usually in a specific div or iframe tag on moontv)
        let iframeUrl = $('iframe').attr('src') || '';
        
        if (!iframeUrl) {
            // Fallback: look for data-src or specific player elements
            iframeUrl = $('#player iframe').attr('src') || '';
        }

        if(iframeUrl && iframeUrl.startsWith('//')) {
            iframeUrl = 'https:' + iframeUrl;
        }

        res.json({ success: true, embedUrl: iframeUrl });

    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/', (req, res) => {
    res.send("MoonTV Scraper API is running on Vercel!");
});

module.exports = app;
