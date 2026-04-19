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
            const title = $(el).find('td[width="100%"] a div').text().trim() || $(el).text().trim();
            let pageLink = $(el).find('a').attr('href');
            let poster = $(el).find('img').attr('src');

            if (title && pageLink && !pageLink.includes('whatsapp') && !pageLink.includes('telegram')) {
                if(!pageLink.startsWith('http')) {
                    pageLink = `https://1filmyfly.fyi${pageLink}`;
                }
                movies.push({ title, pageLink, poster });
            }
        });

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Scraping failed: " + error.message });
    }
});

app.get('/', (req, res) => {
    res.send("FilmyFly Scraper API is running on Vercel!");
});

module.exports = app;
