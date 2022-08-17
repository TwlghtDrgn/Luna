module.exports = {
    name: 'unhandledRejection',
    execute(reason, promise, logger) {
        logger.crit(`!! > Possibly Unhandled Rejection at: ${promise}, ${reason.message}`);
    },
};