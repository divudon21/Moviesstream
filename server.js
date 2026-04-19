const express = require('express');
const cheerio = require('cheerio');
const cors = require('cors');
const puppeteer = require('puppeteer-extra');
const StealthPlugin = require('puppeteer-extra-plugin-stealth');

puppeteer.use(StealthPlugin());

const app = express();
app.use(cors());

const PORT = process.env.PORT || 3000;

app.get('/api/search', async (req, res) => {
    const query = req.query.q;
    if (!query) return res.status(400).json({ error: "Movie name required" });

    let browser;
    try {
        const searchUrl = `https://vegamovies.is/?s=${encodeURIComponent(query)}`;
        
        console.log(`Searching for: ${query}`);

        // Launch Puppeteer (Render environment variables used if available)
        browser = await puppeteer.launch({
            headless: 'new',
            executablePath: process.env.PUPPETEER_EXECUTABLE_PATH || puppeteer.executablePath(),
            args: [
                '--no-sandbox', 
                '--disable-setuid-sandbox',
                '--disable-dev-shm-usage',
                '--disable-accelerated-2d-canvas',
                '--no-first-run',
                '--no-zygote',
                '--single-process', 
                '--disable-gpu'
            ]
        });

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36');

        await page.goto(searchUrl, { waitUntil: 'domcontentloaded', timeout: 30000 });
        
        // Wait to bypass basic Cloudflare checks
        await page.waitForTimeout(5000); 

        const html = await page.content();
        const $ = cheerio.load(html);
        let movies = [];

        $('.blog-items article').each((i, el) => {
            const title = $(el).find('.entry-title a').text().trim();
            const pageLink = $(el).find('.entry-title a').attr('href');
            const poster = $(el).find('.post-thumbnail img').attr('src');

            if (title && pageLink) {
                movies.push({ title, pageLink, poster });
            }
        });

        if (movies.length === 0) {
             return res.json({ success: false, error: "No results found or blocked by Cloudflare Captcha." });
        }

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Puppeteer error: " + error.message });
    } finally {
        if (browser) await browser.close();
    }
});

app.get('/', (req, res) => {
    res.send("Movie Scraper API with Puppeteer is running on Render!");
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
