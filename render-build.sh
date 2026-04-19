#!/usr/bin/env bash
# Exit on error
set -o errexit

npm install

# Store/Caches Puppeteer browser
export PUPPETEER_CACHE_DIR=/opt/render/project/puppeteer
