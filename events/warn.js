module.exports = {
    name: 'warn',
    execute(warn, logger) {
        logger.warn(`!! > Warn: ${warn}`);
    },
};