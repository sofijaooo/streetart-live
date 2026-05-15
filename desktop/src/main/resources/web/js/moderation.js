document.addEventListener("DOMContentLoaded", () => {
    if (!protectPage({
        roles: ["admin"],
        message: "Модерація доступна лише адміністратору"
    })) {
        return;
    }

    loadPendingEvents();
});

async function loadPendingEvents() {
    const container = document.getElementById("pendingEventsList");

    const response = await fetch("http://localhost:8080/api/events/pending");
    const events = await response.json();

    if (!events.length) {
        container.innerHTML = "<p>Немає заявок на модерацію.</p>";
        return;
    }

    container.innerHTML = events.map(event => `
        <div class="moderation-card">
            <h3>${event.place}</h3>
            <p>Дата: ${event.eventDate}</p>
            <p>Час: ${event.time}</p>
            <p>Коментар митця: ${event.comments || "—"}</p>

            <textarea id="comment-${event.id}" placeholder="Причина відхилення"></textarea>

            <button onclick="approveEvent(${event.id})">Узгодити</button>
            <button onclick="rejectEvent(${event.id})">Відхилити</button>
        </div>
    `).join("");
}

async function approveEvent(eventId) {
    await fetch(`http://localhost:8080/api/events/${eventId}/approve`, {
        method: "PUT"
    });

    loadPendingEvents();
}

async function rejectEvent(eventId) {
    const comment = document.getElementById(`comment-${eventId}`).value.trim();

    if (!comment) {
        alert("Вкажіть причину відхилення");
        return;
    }

    await fetch(`http://localhost:8080/api/events/${eventId}/reject`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ comment })
    });

    loadPendingEvents();
}