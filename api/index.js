const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const cors = require('cors');

const app = express();
app.use(cors());

app.get('/api/search', async (req, res) => {
    const query = req.query.q;
    if (!query) return res.status(400).json({ error: "Movie name required" });

    try {
        const searchUrl = `https://1filmyfly.fyi/site-1.html?to-search=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        $('.A10').each((i, el) => {
            let title = $(el).find('div').first().text().trim();
            if (!title) title = $(el).text().replace(/\n/g, '').trim();
            
            let pageLink = $(el).find('a').first().attr('href');
            let poster = $(el).find('img').first().attr('src');

            if (title && pageLink && !pageLink.includes('whatsapp') && !pageLink.includes('telegram')) {
                if(!pageLink.startsWith('http')) {
                    pageLink = `https://1filmyfly.fyi${pageLink}`;
                }
                movies.push({ title, pageLink, poster });
            }
        });

        // Debug: Return raw HTML if empty
        if (movies.length === 0) {
           return res.json({ success: true, movies: [], html_length: data.length, sample: data.substring(0, 500) });
        }

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Scraping failed: " + error.message });
    }
});

app.get('/api/links', async (req, res) => {
    const pageUrl = req.query.url;
    if (!pageUrl) return res.status(400).json({ error: "Page URL required" });

    try {
        const { data } = await axios.get(pageUrl, {
            headers: { 'User-Agent': 'Mozilla/5.0' }
        });
        const $ = cheerio.load(data);
        
        let downloadLinks = [];

        $('a').each((i, el) => {
            const linkText = $(el).text().trim();
            const linkHref = $(el).attr('href');
            
            if (linkHref && linkHref.includes('/download/')) {
                downloadLinks.push({ quality: linkText || "Download", url: `https://1filmyfly.fyi${linkHref}` });
            }
        });

        res.json({ success: true, links: downloadLinks });

    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/', (req, res) => {
    res.send("FilmyFly Scraper API is running on Vercel!");
});

module.exports = app;
