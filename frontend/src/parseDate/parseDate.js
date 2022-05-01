export const parseDate = (written_date) => {
    let d = null;
    if (written_date === null) {
        d = new Date();
    } else {
        d = new Date(written_date);
    }
    const year = d.getFullYear();
    let month = d.getMonth() + 1;
    let date = d.getDate();
    let hours = d.getHours();
    let min = d.getMinutes();
    if (month < 10) {
        month = '0' + month;
    }
    if (date < 10) {
        date = '0' + date;
    }
    if (hours < 10) {
        hours = '0' + hours;
    }
    if (min < 10) {
        min = '0' + min;
    }
    return `${year}-${month}-${date}  ${hours} : ${min}`;
};
