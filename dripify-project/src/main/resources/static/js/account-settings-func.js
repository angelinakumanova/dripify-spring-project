const deactivateButton = document.getElementById('deactivate-btn');
    const deactivateCancelButton = document.getElementById('deactivate-cancel-btn');
    const deactivateModal = document.getElementById('deactivate-modal');
    const deactivateBackdrop = document.getElementById('deactivate-backdrop');
    const deactivatePanel = document.getElementById('deactivate-panel');

    function toggleDeactivateModal() {
      deactivateModal.classList.toggle('pointer-events-none');
      deactivateBackdrop.classList.toggle('opacity-0');
      deactivateBackdrop.classList.toggle('opacity-100');
      deactivatePanel.classList.toggle('opacity-0');
      deactivatePanel.classList.toggle('translate-y-4');
      deactivatePanel.classList.toggle('translate-y-0');
      deactivatePanel.classList.toggle('sm:scale-95');
      deactivatePanel.classList.toggle('sm:scale-100');
    }

    deactivateButton.addEventListener('click', toggleDeactivateModal);
    deactivateCancelButton.addEventListener('click', toggleDeactivateModal);
