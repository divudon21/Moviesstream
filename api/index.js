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
        // Updated search endpoint format for MoonTV
        const searchUrl = `https://moontv.to/search?keyword=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        // Selecting movie items
        $('.item').each((i, el) => {
            const title = $(el).find('.title').text().trim();
            const pageLink = $(el).attr('href') || $(el).find('a').attr('href');
            let poster = $(el).find('img').attr('data-src') || $(el).find('img').attr('src');

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

app.get('/', (req, res) => {
    res.send("MoonTV Scraper API is running on Vercel!");
});

module.exports = app;
