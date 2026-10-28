
const counterElement = document.getElementById("counter");
const progressBar = document.getElementById("progress-bar");
const timeLeftElement = document.getElementById("time-left");
const overlay = document.getElementById("overlay");
const keyElement = document.getElementById("keypressed");

let previousDecrease = null;
let nextDecrease = null;

const eventSource = new EventSource("/events");

eventSource.onmessage = (event) => {
    const data = JSON.parse(event.data);

    counterElement.textContent = data.counter;
    keyElement.textContent = data.keyToPress;

    console.log(data);

    if (data.counter === 0) {
        overlay.style.display = "none";
        previousDecrease = null;
        nextDecrease = null;
        progressBar.style.width = "0%";
        progressBar.classList.remove("executing");
        timeLeftElement.textContent = "--";
        return;
    }

    overlay.style.display = "block";

    previousDecrease = new Date(data.previousDecrease);
    nextDecrease = new Date(data.nextDecrease);

    // Nowy event - przywróć normalny kolor
    progressBar.classList.remove("executing");
};

setInterval(() => {
    if (!previousDecrease || !nextDecrease) {
        return;
    }

    const now = Date.now();

    const start = previousDecrease.getTime();
    const end = nextDecrease.getTime();

    const duration = end - start;
    const elapsed = now - start;
    const remaining = end - now;

    let progress = duration > 0
        ? elapsed / duration
        : 1;

    progress = Math.max(0, Math.min(progress, 1));

    progressBar.style.width = (progress * 100) + "%";

    if (remaining <= 0) {
        // Event trwa - czerwony pulsujący pasek
        progressBar.classList.add("executing");
        timeLeftElement.textContent = "Se klikam...";
    } else {
        progressBar.classList.remove("executing");
        timeLeftElement.textContent =
            (remaining / 1000).toFixed(1) + " s";
    }
}, 50);
