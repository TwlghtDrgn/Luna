module.exports = {
    name: 'unhandledRejection',
    execute(reason, promise) {
        console.log(`!! > Possibly Unhandled Rejection at: ${promise}, ${reason.message}`);
    },
};