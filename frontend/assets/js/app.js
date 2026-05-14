/**
 * Hotel Manager — Frontend JavaScript
 * Animações, interações e lógica de UI.
 */

// === Sidebar Toggle ===
function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('collapsed');
    document.querySelector('.main-content').classList.toggle('expanded');
}

// === Delete Confirmation ===
function confirmDelete(slug, id) {
    Swal.fire({
        title: 'Tem certeza?',
        text: 'Este registro será permanentemente excluído!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#e74c3c',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sim, excluir!',
        cancelButtonText: 'Cancelar',
        background: '#1e1e2e',
        color: '#cdd6f4',
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `?page=${slug}&action=delete&id=${id}`;
        }
    });
}

// === Animate stats on dashboard ===
document.addEventListener('DOMContentLoaded', () => {
    // Animate stat counters
    document.querySelectorAll('.stat-count').forEach((el) => {
        const target = parseInt(el.textContent);
        if (isNaN(target)) return;

        el.textContent = '0';
        let current = 0;
        const increment = Math.max(1, Math.floor(target / 20));
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                current = target;
                clearInterval(timer);
            }
            el.textContent = current;
        }, 40);
    });

    // Staggered card entrance
    document.querySelectorAll('.stat-card, .info-card').forEach((card, i) => {
        card.style.animationDelay = `${i * 0.06}s`;
    });

    // Table row hover glow
    document.querySelectorAll('.data-table tbody tr').forEach((row) => {
        row.addEventListener('mouseenter', () => {
            row.style.transform = 'translateX(4px)';
        });
        row.addEventListener('mouseleave', () => {
            row.style.transform = 'translateX(0)';
        });
    });

    // Mobile sidebar auto-close
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth < 768) {
                document.getElementById('sidebar').classList.add('collapsed');
                document.querySelector('.main-content').classList.add('expanded');
            }
        });
    });
});
