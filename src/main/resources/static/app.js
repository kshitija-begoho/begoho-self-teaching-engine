const form = document.querySelector("#class-form");
const fileNameInput = document.querySelector("#file-name");
const classList = document.querySelector("#class-list");
const template = document.querySelector("#class-template");
const preview = document.querySelector("#code-preview");
const previewName = document.querySelector("#preview-name");
const message = document.querySelector("#message");
const lessonText = document.querySelector("#lesson-text");

function addClass(values = {}) {
    const card = template.content.firstElementChild.cloneNode(true);
    card.querySelector(".access-modifier").value = values.accessModifier ?? "default";
    card.querySelector(".class-name").value = values.className ?? `Class${classList.children.length + 1}`;
    card.querySelector(".has-main").checked = values.isMain ?? false;
    card.querySelector(".remove-class").addEventListener("click", () => {
        card.remove();
        renumberClasses();
        updatePreview();
    });
    card.addEventListener("input", updatePreview);
    classList.append(card);
    renumberClasses();
    updatePreview();
}

function renumberClasses() {
    [...classList.children].forEach((card, index) => {
        card.querySelector(".class-number").textContent = index + 1;
        card.querySelector(".remove-class").hidden = classList.children.length === 1;
    });
}

function getRequest() {
    return {
        fileName: fileNameInput.value.trim(),
        structures: [...classList.children].map(card => ({
            accessModifier: card.querySelector(".access-modifier").value,
            className: card.querySelector(".class-name").value.trim(),
            isMain: card.querySelector(".has-main").checked
        }))
    };
}

function buildCode(structures) {
    return structures.map(structure => {
        const modifier = structure.accessModifier === "public" ? "public " : "";
        const main = structure.isMain
            ? `\n    public static void main(String[] args) {\n        System.out.println("Hello from ${structure.className}!");\n    }\n`
            : "\n";
        return `${modifier}class ${structure.className || "UnnamedClass"} {${main}}\n`;
    }).join("");
}

function updatePreview() {
    const request = getRequest();
    previewName.textContent = request.fileName || "Untitled.java";
    preview.textContent = buildCode(request.structures);
}

document.querySelector("#add-class").addEventListener("click", () => addClass());
fileNameInput.addEventListener("input", updatePreview);

form.addEventListener("submit", async event => {
    event.preventDefault();
    message.className = "message";
    message.textContent = "Generating...";

    try {
        const response = await fetch("/v1/file-structure/generate", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(getRequest())
        });
        const result = await response.json();
        if (!response.ok) {
            throw new Error(result.error ?? "The file could not be generated.");
        }

        previewName.textContent = result.fileName;
        preview.textContent = result.generatedCode;
        lessonText.textContent = result.lesson;
        message.className = "message success";
        message.textContent = `${result.fileName} was generated successfully.`;
    } catch (error) {
        message.className = "message error";
        message.textContent = error.message;
    }
});

addClass({accessModifier: "public", className: "First", isMain: true});
