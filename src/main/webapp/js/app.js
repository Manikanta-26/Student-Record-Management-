/* ============================================================
   Student Record Management System — Frontend JS
   ============================================================ */

// ── Modal helpers ──────────────────────────────────────────────
function openModal() {
  document.getElementById('addModal').style.display = 'flex';
}

function closeModal() {
  document.getElementById('addModal').style.display = 'none';
}

// Close modal on overlay click
document.addEventListener('DOMContentLoaded', () => {
  const overlay = document.getElementById('addModal');
  if (overlay) {
    overlay.addEventListener('click', (e) => {
      if (e.target === overlay) closeModal();
    });
  }

  // ── CGPA badge colouring ──────────────────────────────────────
  document.querySelectorAll('.cgpa-value').forEach(el => {
    const val = parseFloat(el.textContent);
    if (val >= 8.0)      el.classList.add('cgpa-high');
    else if (val >= 6.0) el.classList.add('cgpa-mid');
    else                 el.classList.add('cgpa-low');
    el.classList.add('cgpa-badge');
  });

  // ── Delete confirmation ───────────────────────────────────────
  document.querySelectorAll('.action-delete').forEach(link => {
    link.addEventListener('click', (e) => {
      if (!confirm('Are you sure you want to delete this student record?')) {
        e.preventDefault();
      }
    });
  });

  // ── Client-side search filter (table) ─────────────────────────
  const searchInput = document.getElementById('liveSearch');
  if (searchInput) {
    searchInput.addEventListener('input', () => {
      const q = searchInput.value.toLowerCase();
      document.querySelectorAll('#tableBody tr').forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
    });
  }

  // ── Form validation feedback ──────────────────────────────────
  const addForm = document.getElementById('addForm');
  if (addForm) {
    addForm.addEventListener('submit', (e) => {
      const cgpa = parseFloat(addForm.querySelector('[name=cgpa]').value);
      if (isNaN(cgpa) || cgpa < 0 || cgpa > 10) {
        e.preventDefault();
        alert('CGPA must be between 0.00 and 10.00');
        return;
      }
      const roll = addForm.querySelector('[name=roll_number]').value;
      if (!/^[A-Z]{2,3}\d{7}$/.test(roll)) {
        e.preventDefault();
        alert('Roll number format: 2-3 uppercase letters + 7 digits (e.g. CS2021001)');
      }
    });
  }
});
