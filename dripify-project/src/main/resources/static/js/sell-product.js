const fileInput = document.getElementById("images");
const previewContainer = document.getElementById("preview");
const maxFiles = 5;
let uploadedFiles = []; // To keep track of uploaded files

fileInput.addEventListener("change", function (event) {
  const newFiles = Array.from(event.target.files);

  if (uploadedFiles.length + newFiles.length > maxFiles) {
    return;
  }

  newFiles.forEach((file) => {
    if (!["image/jpeg", "image/png"].includes(file.type)) {
      return;
    }

    uploadedFiles.push(file);
    updatePreview(file);
  });

  // Clear the input so the same file can be re-uploaded if needed
  event.target.value = "";
});

function updatePreview(file) {
  const reader = new FileReader();
  reader.onload = function (e) {
    const img = document.createElement("img");
    img.src = e.target.result;
    img.alt = "Uploaded Image";
    img.classList.add("w-30", "h-30", "object-cover", "rounded");

    const removeButton = document.createElement("button");
    removeButton.textContent = "Remove";
    removeButton.classList.add("text-white", "font-semibld", "text-sm", "mt-2", "bg-red-500", "rounded-full", "p-2");
    removeButton.addEventListener("click", () => {
      const index = uploadedFiles.indexOf(file);
      if (index > -1) {
        uploadedFiles.splice(index, 1);
      }
      previewContainer.removeChild(wrapper);
    });

    const wrapper = document.createElement("div");
    wrapper.classList.add("flex", "flex-col", "items-center");
    wrapper.appendChild(img);
    wrapper.appendChild(removeButton);

    previewContainer.appendChild(wrapper);
  };
  reader.readAsDataURL(file);
}
