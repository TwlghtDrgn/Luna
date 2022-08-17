module.exports = {
    name: 'error',
    execute(error, logger) {
        logger.error(`!! > Error: ${error}`);
    },
};