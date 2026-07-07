const counterElement = document.getElementById("counter");
const progressBar = document.getElementById("progress-bar");
const timeLeftElement = document.getElementById("time-left");

let previousDecrease = null;
let nextDecrease = null;

const eventSource = new EventSource("/events");
const overlay = document.getElementById("overlay");
const keyElement = document.getElementById("keypressed");
eventSource.onmessage = (event) => {

    const data = JSON.parse(event.data);

    counterElement.textContent = data.counter;
     keyElement.textContent = data.keyToPress; // dodaj
   console.log(data);
    if (data.counter === 0) {
        overlay.style.display = "none";
        previousDecrease = null;
        nextDecrease = null;
        progressBar.style.width = "0%";
        timeLeftElement.textContent = "--";
        return;
    }
    overlay.style.display = "block";
    previousDecrease = new Date(data.previousDecrease);
    nextDecrease = new Date(data.nextDecrease);
};

setInterval(() => {

    if (!previousDecrease || !nextDecrease)
        return;

    const now = Date.now();

    const start = previousDecrease.getTime();
    const end = nextDecrease.getTime();

    const duration = end - start;
    const elapsed = now - start;
    const remaining = end - now;

    let progress = elapsed / duration;

    progress = Math.max(0, Math.min(progress, 1));

    progressBar.style.width = (progress * 100) + "%";

    timeLeftElement.textContent =
        Math.max(0, (remaining / 1000).toFixed(1)) + " s";

}, 50);