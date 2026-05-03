document.addEventListener("DOMContentLoaded", () => {
    if (!protectPage({
        message: "Акаунт доступний після входу або реєстрації"
    })) {
        return;
    }

    const user = getCurrentUser();

    document.getElementById("accountName").value = user.name || "";
    document.getElementById("accountSurname").value = user.surname || "";
    document.getElementById("accountPhone").value = user.phone || "";
    document.getElementById("accountUsername").value = user.username || "";
    document.getElementById("accountEmail").value = user.email || "";

    if (user.birthDate) {
        document.getElementById("accountBirthDate").value = formatDateForInput(user.birthDate);
    }
    const userId = user.id || user.userId;
    const form = document.getElementById("accountForm");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const data = {
            name: document.getElementById("accountName").value.trim(),
            surname: document.getElementById("accountSurname").value.trim(),
            birthDate: document.getElementById("accountBirthDate").value.trim(),
            phone: document.getElementById("accountPhone").value.trim(),
            username: document.getElementById("accountUsername").value.trim(),
            email: document.getElementById("accountEmail").value.trim()
        };
        const error = validateAccountForm(data);

        if (error) {
            showAuthError(error);
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            const result = await response.json();

            if (!response.ok) {
                console.log("UPDATE ERROR:", JSON.stringify(result, null, 2));
                showAuthError(result.message || "Не вдалося оновити дані");
                return;
            }

            setCurrentUser(result);
            showAuthError("Дані успішно оновлено");

        } catch (error) {
            showAuthError("Сервер недоступний");
        }
    });
});

function formatDateForInput(date) {
    if (!date) return "";

    if (date.includes(".")) return date;

    const parts = date.split("-");
    if (parts.length !== 3) return date;

    return `${parts[2]}.${parts[1]}.${parts[0]}`;
}

function validateAccountForm(data) {

    if (!/^[A-Za-zА-Яа-яІіЇїЄє'\- ]{2,}$/.test(data.name)) {
        return "Ім’я має містити лише літери (мінімум 2 символи)";
    }

    if (!/^[A-Za-zА-Яа-яІіЇїЄє'\- ]{2,}$/.test(data.surname)) {
        return "Прізвище має містити лише літери (мінімум 2 символи)";
    }

    if (!/^\+\d{10,15}$/.test(data.phone)) {
        return "Телефон має починатися з + та містити 10–15 цифр";
    }

    if (!data.birthDate) {
        return "Введіть дату народження";
    }

    try {
        // формат dd.MM.yyyy
        const parts = data.birthDate.split(".");
        if (parts.length !== 3) throw new Error();

        const day = parseInt(parts[0], 10);
        const month = parseInt(parts[1], 10) - 1;
        const year = parseInt(parts[2], 10);

        const birthDate = new Date(year, month, day);

        // проверка корректности даты
        if (
            birthDate.getFullYear() !== year ||
            birthDate.getMonth() !== month ||
            birthDate.getDate() !== day
        ) {
            throw new Error();
        }

        const today = new Date();

        if (birthDate > today) {
            return "Дата народження не може бути в майбутньому";
        }

        const minAgeDate = new Date();
        minAgeDate.setFullYear(today.getFullYear() - 10);

        if (birthDate > minAgeDate) {
            return "Мінімальний вік — 10 років";
        }

    } catch (e) {
        return "Дата народження має бути у форматі 31.01.2000";
    }

    if (data.username.length < 2) {
        return "Нікнейм має містити мінімум 2 символи";
    }

    if (!/^[\w.-]+@[\w.-]+\.[A-Za-z]{2,}$/.test(data.email)) {
        return "Введіть коректний email";
    }

    return null;
}