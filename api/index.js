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
        // Scrape 1filmyfly.fyi
        const searchUrl = `https://1filmyfly.fyi/site-1.html?to-search=${encodeURIComponent(query)}`;
        const { data } = await axios.get(searchUrl, {
            headers: {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36'
            }
        });
        
        const $ = cheerio.load(data);
        let movies = [];

        $('.A10 table tr').each((i, el) => {
            const title = $(el).find('td[width="100%"] a div').text().trim();
            let pageLink = $(el).find('td[width="63"] a').attr('href');
            let poster = $(el).find('td[width="63"] img').attr('src');

            if (title && pageLink) {
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

app.get('/api/get-links', async (req, res) => {
    const pageUrl = req.query.url;
    if (!pageUrl) return res.status(400).json({ error: "Page URL required" });

    try {
        const { data } = await axios.get(pageUrl, {
            headers: { 'User-Agent': 'Mozilla/5.0' }
        });
        const $ = cheerio.load(data);
        
        let downloadLinks = [];

        // Find download links on the movie page
        $('a[href*="download"]').each((i, el) => {
            const linkText = $(el).text().trim();
            const linkHref = $(el).attr('href');
            
            if (linkHref && !linkHref.includes('.jpg') && !linkHref.includes('.png')) {
                downloadLinks.push({ quality: linkText || "Download", url: linkHref });
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
