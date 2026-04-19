const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const cors = require('cors');

const app = express();
app.use(cors());

// TheMovieBox search
app.get('/api/search', async (req, res) => {
    const query = req.query.q;
    if (!query) return res.status(400).json({ error: "Movie name required" });

    try {
        const searchUrl = `https://v.moviebox.ph/search?q=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        // Updated for moviebox.ph
        $('.movie-card, .item, article').each((i, el) => {
            const title = $(el).find('.title, h2, h3').text().trim();
            const pageLink = $(el).attr('href') || $(el).find('a').attr('href');
            let poster = $(el).find('img').attr('src') || $(el).find('img').attr('data-src');

            if (title && pageLink) {
                if(pageLink.startsWith('/')) {
                    movies.push({ title, pageLink: `https://v.moviebox.ph${pageLink}`, poster });
                } else {
                    movies.push({ title, pageLink, poster });
                }
            }
        });

        // Fallback to moontv.to if moviebox fails
        if (movies.length === 0) {
            const moonUrl = `https://moontv.to/filter?keyword=${encodeURIComponent(query)}`;
            const moonData = await axios.get(moonUrl, {
                headers: { 'User-Agent': 'Mozilla/5.0' }
            });
            const $moon = cheerio.load(moonData.data);
            
            $moon('.item').each((i, el) => {
                const title = $moon(el).find('.title').text().trim();
                const pageLink = $moon(el).attr('href') || $moon(el).find('a').attr('href');
                let poster = $moon(el).find('img').attr('data-src') || $moon(el).find('img').attr('src');

                if (title && pageLink) {
                    if(pageLink.startsWith('/')) {
                        movies.push({ title, pageLink: `https://moontv.to${pageLink}`, poster });
                    } else {
                        movies.push({ title, pageLink, poster });
                    }
                }
            });
        }

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Scraping failed: " + error.message });
    }
});

// Extract video link from page
app.get('/api/get-links', async (req, res) => {
    const pageUrl = req.query.url;
    if (!pageUrl) return res.status(400).json({ error: "Page URL required" });

    try {
        const { data } = await axios.get(pageUrl, {
            headers: { 'User-Agent': 'Mozilla/5.0' }
        });
        const $ = cheerio.load(data);
        
        let iframeUrl = $('iframe').attr('src');
        
        // If it's moontv.to, they might use data-src or scripts
        if(!iframeUrl) {
            iframeUrl = $('iframe').attr('data-src');
        }

        res.json({ success: true, iframeUrl: iframeUrl || "Not found" });

    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/', (req, res) => {
    res.send("Movie Scraper API is running on Vercel!");
});

module.exports = app;
