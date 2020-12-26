const {setHeadlessWhen} = require('@codeceptjs/configure');

// turn on headless mode when running with HEADLESS=true environment variable
// export HEADLESS=true && npx codeceptjs run
setHeadlessWhen(process.env.HEADLESS);

exports.config = {
    tests: './*_test.js',
    output: './output',
    helpers: {
        Puppeteer: {
            url: 'http://localhost:9080',
            show: false,
            windowSize: '1200x900',
            chrome: {
                args: ["--headless", "--no-sandbox"]
            }
        }
    },
    include: {
        I: './steps_file.js',
        utils: './utils.js'
    },
    bootstrap: null,
    mocha: {},
    name: 'e2e',
    plugins: {
        retryFailedStep: {
            enabled: true
        },
        screenshotOnFail: {
            enabled: true
        }
    },
}