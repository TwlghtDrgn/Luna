module.exports = {
    name: 'uncaughtException',
    execute(error) {
        console.log(`!! > Uncaught Exception: ${error}`);
        process.exit(1);
    },
};