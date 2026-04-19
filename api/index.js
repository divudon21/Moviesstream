const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const cors = require('cors');

const app = express();
app.use(cors());

// 1. Search API using MoonTV
app.get('/api/search', async (req, res) => {
    const query = req.query.q;
    if (!query) return res.status(400).json({ error: "Movie name required" });

    try {
        const searchUrl = `https://moontv.to/filter?keyword=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        $('.item').each((i, el) => {
            const title = $(el).find('.title').text().trim();
            const pageLink = $(el).attr('href');
            let poster = $(el).find('img').attr('data-src') || $(el).find('img').attr('src');

            // Construct absolute URL for poster if it's relative
            if(poster && poster.startsWith('/')) {
                poster = `https://moontv.to${poster}`;
            }

            if (title && pageLink) {
                movies.push({ 
                    title, 
                    pageLink: pageLink.startsWith('/') ? `https://moontv.to${pageLink}` : pageLink, 
                    poster 
                });
            }
        });

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Scraping failed: " + error.message });
    }
});

// 2. Get Stream URL from MoonTV movie page
app.get('/api/stream', async (req, res) => {
    const pageUrl = req.query.url;
    if (!pageUrl) return res.status(400).json({ error: "Page URL required" });

    try {
        const { data } = await axios.get(pageUrl, {
            headers: { 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)' }
        });
        
        // Moontv uses a specific iframe or script to load the player
        const $ = cheerio.load(data);
        
        // Try to find iframe src
        let iframeSrc = $('iframe').attr('src');
        
        // If no iframe, look for data-src or scripts
        if(!iframeSrc) {
            iframeSrc = $('iframe').attr('data-src');
        }

        if(iframeSrc) {
            if(iframeSrc.startsWith('//')) iframeSrc = 'https:' + iframeSrc;
            return res.json({ success: true, streamUrl: iframeSrc, type: "iframe" });
        }

        res.json({ success: false, error: "Stream link not found on page" });

    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/', (req, res) => {
    res.send("MoonTV Scraper API is running on Vercel!");
});

module.exports = app;
