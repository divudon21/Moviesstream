const express = require('express');
const cheerio = require('cheerio');
const cors = require('cors');
const puppeteer = require('puppeteer-core');
const chromium = require('@sparticuz/chromium');
const { addExtra } = require('puppeteer-extra');
const StealthPlugin = require('puppeteer-extra-plugin-stealth');

const puppeteerExtra = addExtra(puppeteer);
puppeteerExtra.use(StealthPlugin());

const app = express();
app.use(cors());

// Vercel serverless function export
app.get('/api/search', async (req, res) => {
    const query = req.query.q;
    if (!query) return res.status(400).json({ error: "Movie name required" });

    let browser;
    try {
        const searchUrl = `https://vegamovies.is/?s=${encodeURIComponent(query)}`;
        
        // Setup Chromium for Vercel Serverless
        browser = await puppeteerExtra.launch({
            args: chromium.args,
            defaultViewport: chromium.defaultViewport,
            executablePath: await chromium.executablePath(),
            headless: chromium.headless,
            ignoreHTTPSErrors: true,
        });

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36');

        await page.goto(searchUrl, { waitUntil: 'domcontentloaded', timeout: 25000 });
        await page.waitForTimeout(3000); // Wait for CF

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
             return res.json({ success: false, error: "No results found or blocked by Cloudflare." });
        }

        res.json({ success: true, movies });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: "Vercel Puppeteer error: " + error.message });
    } finally {
        if (browser) {
            await browser.close();
        }
    }
});

app.get('/', (req, res) => {
    res.send("Movie Scraper API is running on Vercel!");
});

// For local dev testing
if (process.env.NODE_ENV !== 'production') {
    const PORT = process.env.PORT || 3000;
    app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
}

module.exports = app;
