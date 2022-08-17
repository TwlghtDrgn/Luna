module.exports = {
    name: 'uncaughtException',
    execute(error, logger) {
        logger.error(`!! > Uncaught Exception: ${error}`);
    },
};