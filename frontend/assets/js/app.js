/**
 * Hotel Manager — Frontend JavaScript
 * Tema, máscaras, upload de imagem, animações.
 */

// === Tema ===
function syncThemeToggle() {
    const btn = document.getElementById('theme-toggle-btn');
    if (!btn) return;
    const isLight = document.documentElement.getAttribute('data-theme') === 'light';
    btn.setAttribute('aria-pressed', isLight ? 'true' : 'false');
}

function toggleTheme() {
    const root = document.documentElement;
    const current = root.getAttribute('data-theme') === 'light' ? 'light' : 'dark';
    const next = current === 'light' ? 'dark' : 'light';
    root.setAttribute('data-theme', next);
    localStorage.setItem('hm-theme', next);
    syncThemeToggle();
}

function swalTheme() {
    const isLight = document.documentElement.getAttribute('data-theme') === 'light';
    return isLight
        ? { background: '#ffffff', color: '#1c1f2a' }
        : { background: '#1e1e2e', color: '#cdd6f4' };
}

// === Sidebar Toggle ===
function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('collapsed');
    document.querySelector('.main-content').classList.toggle('expanded');
}

// === Delete Confirmation ===
function confirmDelete(slug, id) {
    const isLight = document.documentElement.getAttribute('data-theme') === 'light';
    Swal.fire({
        title: 'Tem certeza?',
        text: 'Este registro será permanentemente excluído!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: isLight ? '#c92a4c' : '#f38ba8',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sim, excluir!',
        cancelButtonText: 'Cancelar',
        ...swalTheme(),
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `?page=${slug}&action=delete&id=${id}`;
        }
    });
}

// === Máscara monetária BR (mantém só dígitos, exibe 1.234,56) ===
function applyMoneyMask(input) {
    const format = (raw) => {
        const digits = (raw || '').toString().replace(/\D/g, '');
        if (!digits) return '';
        const cents = digits.padStart(3, '0');
        const intPart = cents.slice(0, -2).replace(/^0+/, '') || '0';
        const dec = cents.slice(-2);
        return intPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + ',' + dec;
    };

    // Valor inicial pode vir como "1234.56" do PHP; reformata para BR.
    if (input.value) {
        const num = parseFloat(input.value.replace(',', '.'));
        if (!isNaN(num)) {
            input.value = format(Math.round(num * 100).toString());
        }
    }

    input.addEventListener('input', () => {
        input.value = format(input.value);
    });
}

// === Image upload: alterna entre arquivo e URL ===
function initImageUpload(wrapper) {
    const fileInput = wrapper.querySelector('.input-file');
    const urlInput  = wrapper.querySelector('.input-url');
    const preview   = wrapper.querySelector('.preview');
    const tabUp     = wrapper.querySelector('.tab-upload');
    const tabUrl    = wrapper.querySelector('.tab-url');

    const showMode = (mode) => {
        const isUpload = mode === 'upload';
        fileInput.style.display = isUpload ? '' : 'none';
        urlInput.style.display  = isUpload ? 'none' : '';
        tabUp.classList.toggle('active', isUpload);
        tabUrl.classList.toggle('active', !isUpload);
        tabUp.setAttribute('aria-selected', isUpload ? 'true' : 'false');
        tabUrl.setAttribute('aria-selected', !isUpload ? 'true' : 'false');
        fileInput.disabled = !isUpload;
        urlInput.disabled  = isUpload;
    };

    tabUp.addEventListener('click',  () => showMode('upload'));
    tabUrl.addEventListener('click', () => showMode('url'));

    fileInput.addEventListener('change', () => {
        const f = fileInput.files && fileInput.files[0];
        if (!f) return;
        const reader = new FileReader();
        reader.onload = (e) => {
            preview.innerHTML = `<img src="${e.target.result}" alt="preview">`;
        };
        reader.readAsDataURL(f);
    });

    urlInput.addEventListener('input', () => {
        const v = urlInput.value.trim();
        if (v) preview.innerHTML = `<img src="${v}" alt="preview" onerror="this.parentNode.innerHTML='<span>URL inválida</span>'">`;
    });

    // Default: se já tem URL preenchida (modo edit), inicia em modo URL.
    if (urlInput.value && !fileInput.files?.length) {
        showMode('url');
    } else {
        showMode('upload');
    }
}

// === Loading state no submit do crud-form ===
function initFormLoading() {
    document.querySelectorAll('form.crud-form').forEach((form) => {
        form.addEventListener('submit', () => {
            const btn = form.querySelector('button[type="submit"].btn-primary');
            if (btn && !btn.disabled) btn.classList.add('is-loading');
        });
    });
}

// === Listagem: busca + sort + paginação (client-side) ===
function initListTable() {
    const root = document.querySelector('[data-list-root]');
    if (!root) return;

    const tbody    = root.querySelector('[data-list-tbody]');
    const allRows  = tbody ? Array.from(tbody.querySelectorAll('[data-row]')) : [];
    if (!allRows.length) return;

    const search    = root.querySelector('[data-list-search]');
    const countEl   = root.querySelector('[data-record-count]');
    const totalText = countEl ? countEl.parentNode.textContent.match(/de (\d+)/)?.[1] : allRows.length;
    const emptyEl   = root.querySelector('[data-empty-search]');
    const paginEl   = root.querySelector('[data-pagination]');
    const pageSizeSel = root.querySelector('[data-page-size]');
    const prevBtn   = root.querySelector('[data-page-prev]');
    const nextBtn   = root.querySelector('[data-page-next]');
    const pageCurEl = root.querySelector('[data-page-current]');
    const pageTotEl = root.querySelector('[data-page-total]');

    let state = {
        filtered: allRows.slice(),
        page: 1,
        pageSize: pageSizeSel ? parseInt(pageSizeSel.value, 10) : 25,
        sortKey: null,
        sortDir: null,
        sortType: 'string',
    };

    const norm = (s) => (s || '').toString().toLowerCase().normalize('NFD').replace(/[̀-ͯ]/g, '');

    const compareValues = (a, b, type) => {
        if (a === '' && b !== '') return 1;
        if (b === '' && a !== '') return -1;
        if (type === 'number') {
            const na = parseFloat(String(a).replace(',', '.')) || 0;
            const nb = parseFloat(String(b).replace(',', '.')) || 0;
            return na - nb;
        }
        if (type === 'date') {
            return new Date(a).getTime() - new Date(b).getTime();
        }
        return String(a).localeCompare(String(b), 'pt-BR', { numeric: true, sensitivity: 'base' });
    };

    const applyFilter = () => {
        const q = norm(search ? search.value.trim() : '');
        state.filtered = q
            ? allRows.filter(r => norm(r.dataset.search).includes(q))
            : allRows.slice();
        state.page = 1;
    };

    const applySort = () => {
        if (!state.sortKey || !state.sortDir) return;
        const key = state.sortKey;
        const type = state.sortType;
        const dir = state.sortDir === 'asc' ? 1 : -1;
        // HTML lowercase attr names, então usar getAttribute com lowercase.
        const attr = `data-sort-${key.toLowerCase()}`;
        state.filtered.sort((ra, rb) => {
            const va = ra.getAttribute(attr) ?? '';
            const vb = rb.getAttribute(attr) ?? '';
            return compareValues(va, vb, type) * dir;
        });
    };

    const render = () => {
        const total = state.filtered.length;
        const totalPages = Math.max(1, Math.ceil(total / state.pageSize));
        if (state.page > totalPages) state.page = totalPages;
        const start = (state.page - 1) * state.pageSize;
        const end = start + state.pageSize;

        allRows.forEach(r => { r.style.display = 'none'; });
        state.filtered.slice(start, end).forEach((r, i) => {
            r.style.display = '';
            r.style.setProperty('--i', i);
        });

        if (countEl) countEl.textContent = total;
        if (emptyEl) emptyEl.hidden = total > 0;
        const tableEl = root.querySelector('[data-list-table]');
        if (tableEl) tableEl.style.display = total > 0 ? '' : 'none';

        if (paginEl) {
            paginEl.hidden = total <= state.pageSize;
            if (pageCurEl) pageCurEl.textContent = state.page;
            if (pageTotEl) pageTotEl.textContent = totalPages;
            if (prevBtn) prevBtn.disabled = state.page <= 1;
            if (nextBtn) nextBtn.disabled = state.page >= totalPages;
        }
    };

    if (search) {
        let timer;
        search.addEventListener('input', () => {
            clearTimeout(timer);
            timer = setTimeout(() => { applyFilter(); applySort(); render(); }, 120);
        });
        // Atalho "/" foca a busca
        document.addEventListener('keydown', (e) => {
            if (e.key === '/' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(document.activeElement.tagName)) {
                e.preventDefault();
                search.focus();
            }
        });
    }

    root.querySelectorAll('.th-sortable').forEach(th => {
        th.querySelector('.sort-btn')?.addEventListener('click', () => {
            const key = th.dataset.sortKey;
            const type = th.dataset.sortType || 'string';
            if (state.sortKey === key) {
                state.sortDir = state.sortDir === 'asc' ? 'desc' : (state.sortDir === 'desc' ? null : 'asc');
                if (!state.sortDir) state.sortKey = null;
            } else {
                state.sortKey = key;
                state.sortType = type;
                state.sortDir = 'asc';
            }
            root.querySelectorAll('.th-sortable').forEach(x => x.removeAttribute('data-sort-active'));
            if (state.sortKey) th.setAttribute('data-sort-active', state.sortDir);

            applyFilter();
            applySort();
            render();
        });
    });

    if (pageSizeSel) pageSizeSel.addEventListener('change', () => {
        state.pageSize = parseInt(pageSizeSel.value, 10);
        state.page = 1;
        render();
    });
    if (prevBtn) prevBtn.addEventListener('click', () => { if (state.page > 1) { state.page--; render(); } });
    if (nextBtn) nextBtn.addEventListener('click', () => {
        const totalPages = Math.max(1, Math.ceil(state.filtered.length / state.pageSize));
        if (state.page < totalPages) { state.page++; render(); }
    });

    render();
}

// === Boot ===
document.addEventListener('DOMContentLoaded', () => {
    syncThemeToggle();
    initFormLoading();
    initListTable();
    document.querySelectorAll('input[data-money="1"]').forEach(applyMoneyMask);
    document.querySelectorAll('[data-image-field]').forEach(initImageUpload);

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

    document.querySelectorAll('.stat-card, .info-card').forEach((card, i) => {
        card.style.animationDelay = `${i * 0.06}s`;
    });

    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth < 768) {
                document.getElementById('sidebar').classList.add('collapsed');
                document.querySelector('.main-content').classList.add('expanded');
            }
        });
    });
});
