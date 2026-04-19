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
        const searchUrl = `https://themoviebox.xyz/search?q=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        // Note: TheMovieBox might use a different HTML structure, adjusting based on common patterns
        $('a.movie-card, .movie-card, article').each((i, el) => {
            const title = $(el).find('.title, h2, h3').text().trim();
            const pageLink = $(el).attr('href') || $(el).find('a').attr('href');
            let poster = $(el).find('img').attr('src') || $(el).find('img').attr('data-src');

            if (title && pageLink) {
                if(pageLink.startsWith('/')) {
                    movies.push({ title, pageLink: `https://themoviebox.xyz${pageLink}`, poster });
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

app.get('/', (req, res) => {
    res.send("TheMovieBox Scraper API is running on Vercel!");
});

module.exports = app;
