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

// === Toast notifications ===
function showToast(kind, message, opts = {}) {
    const container = document.getElementById('toast-container');
    if (!container) return;
    const duration = opts.duration ?? (kind === 'error' ? 5000 : 2800);
    const icons = { success: 'fa-check', error: 'fa-xmark', info: 'fa-info' };
    const icon = icons[kind] || 'fa-info';

    const el = document.createElement('div');
    el.className = `toast toast-${kind}`;
    el.setAttribute('role', kind === 'error' ? 'alert' : 'status');
    el.innerHTML = `
        <div class="toast-icon"><i class="fas ${icon}" aria-hidden="true"></i></div>
        <div class="toast-body"></div>
        <button type="button" class="toast-close" aria-label="Fechar"><i class="fas fa-xmark" aria-hidden="true"></i></button>
    `;
    el.querySelector('.toast-body').textContent = message;
    container.appendChild(el);

    const dismiss = () => {
        el.classList.add('is-leaving');
        setTimeout(() => el.remove(), 240);
    };
    el.querySelector('.toast-close').addEventListener('click', dismiss);
    if (duration > 0) setTimeout(dismiss, duration);
}

// === Sidebar Toggle (mobile: hide, desktop: collapse icon-only) ===
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const main = document.querySelector('.main-content');
    const backdrop = document.querySelector('[data-sidebar-backdrop]');
    const isMobile = window.innerWidth < 769;
    const willCollapse = !sidebar.classList.contains('collapsed');

    sidebar.classList.toggle('collapsed');
    main.classList.toggle('expanded');

    if (isMobile && backdrop) {
        backdrop.hidden = willCollapse;
        requestAnimationFrame(() => backdrop.classList.toggle('is-visible', !willCollapse));
    }

    if (!isMobile) {
        localStorage.setItem('hm-sidebar-collapsed', willCollapse ? '1' : '0');
    }
}

function restoreSidebarState() {
    if (window.innerWidth >= 769 && localStorage.getItem('hm-sidebar-collapsed') === '1') {
        document.getElementById('sidebar')?.classList.add('collapsed');
        document.querySelector('.main-content')?.classList.add('expanded');
    } else if (window.innerWidth < 769) {
        // Mobile sempre começa colapsado
        document.getElementById('sidebar')?.classList.add('collapsed');
        document.querySelector('.main-content')?.classList.add('expanded');
    }
}

function initSidebarBackdrop() {
    const backdrop = document.querySelector('[data-sidebar-backdrop]');
    if (!backdrop) return;
    backdrop.addEventListener('click', () => toggleSidebar());
}

// === Command Palette (Ctrl+K) ===
function openCommandPalette() {
    const overlay = document.getElementById('cmdk-overlay');
    if (!overlay) return;
    overlay.hidden = false;
    const input = document.getElementById('cmdk-input');
    input.value = '';
    filterCmdkItems('');
    setTimeout(() => input.focus(), 30);
}

function closeCommandPalette() {
    const overlay = document.getElementById('cmdk-overlay');
    if (overlay) overlay.hidden = true;
}

function filterCmdkItems(q) {
    const norm = (s) => (s || '').toLowerCase().normalize('NFD').replace(/[̀-ͯ]/g, '');
    const query = norm(q.trim());
    const items = document.querySelectorAll('.cmdk-item');
    const empty = document.getElementById('cmdk-empty');
    let visibleCount = 0;
    let firstVisible = null;

    items.forEach((item) => {
        const text = norm(item.textContent);
        const match = !query || text.includes(query);
        item.hidden = !match;
        item.classList.remove('is-active');
        if (match) {
            visibleCount++;
            if (!firstVisible) firstVisible = item;
        }
    });

    if (firstVisible) firstVisible.classList.add('is-active');
    if (empty) empty.hidden = visibleCount > 0;
}

function moveCmdkSelection(direction) {
    const items = Array.from(document.querySelectorAll('.cmdk-item:not([hidden])'));
    if (!items.length) return;
    const currentIdx = items.findIndex(i => i.classList.contains('is-active'));
    items.forEach(i => i.classList.remove('is-active'));
    let next = currentIdx + direction;
    if (next < 0) next = items.length - 1;
    if (next >= items.length) next = 0;
    items[next].classList.add('is-active');
    items[next].scrollIntoView({ block: 'nearest' });
}

function executeCmdkAction(cmd) {
    if (cmd === '__theme__') {
        toggleTheme();
        closeCommandPalette();
        return;
    }
    if (cmd) window.location.href = cmd;
}

function initCommandPalette() {
    const overlay = document.getElementById('cmdk-overlay');
    if (!overlay) return;
    const input = document.getElementById('cmdk-input');

    input.addEventListener('input', () => filterCmdkItems(input.value));

    input.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowDown') { e.preventDefault(); moveCmdkSelection(1); }
        else if (e.key === 'ArrowUp') { e.preventDefault(); moveCmdkSelection(-1); }
        else if (e.key === 'Enter') {
            e.preventDefault();
            const active = overlay.querySelector('.cmdk-item.is-active');
            if (active) executeCmdkAction(active.dataset.cmd);
        }
        else if (e.key === 'Escape') closeCommandPalette();
    });

    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) closeCommandPalette();
    });

    document.querySelectorAll('.cmdk-item').forEach((item) => {
        item.addEventListener('click', () => executeCmdkAction(item.dataset.cmd));
        item.addEventListener('mouseenter', () => {
            overlay.querySelectorAll('.cmdk-item').forEach(i => i.classList.remove('is-active'));
            item.classList.add('is-active');
        });
    });

    // Atalho global Ctrl+K / Cmd+K
    document.addEventListener('keydown', (e) => {
        if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
            e.preventDefault();
            if (overlay.hidden) openCommandPalette();
            else closeCommandPalette();
        }
    });
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
        form.addEventListener('submit', (e) => {
            // Re-valida tudo antes de enviar; bloqueia se houver erro.
            const fields = form.querySelectorAll('.form-input, .form-select');
            let firstInvalid = null;
            fields.forEach((f) => {
                if (!validateField(f) && !firstInvalid) firstInvalid = f;
            });
            if (firstInvalid) {
                e.preventDefault();
                firstInvalid.focus();
                firstInvalid.closest('.form-group')?.classList.add('has-error');
                setTimeout(() => firstInvalid.closest('.form-group')?.classList.remove('has-error'), 500);
                return;
            }
            const btn = form.querySelector('button[type="submit"].btn-primary');
            if (btn && !btn.disabled) btn.classList.add('is-loading');
        });
    });
}

// === Validação inline (on blur + on submit) ===
function validateField(field) {
    // Pula campos que não são validáveis (file, hidden ainda sem valor obrigatório)
    if (field.type === 'file' || field.disabled) return true;

    const group = field.closest('.form-group');
    if (!group) return true;

    const value = (field.value || '').trim();
    const isRequired = field.hasAttribute('required');

    let error = null;

    if (isRequired && !value) {
        error = 'Obrigatório.';
    } else if (value) {
        // Money: deve casar com regex simples + ser número válido
        if (field.dataset.money === '1') {
            const numeric = value.replace(/\./g, '').replace(',', '.');
            if (isNaN(parseFloat(numeric)) || parseFloat(numeric) < 0) {
                error = 'Valor monetário inválido.';
            }
        } else if (field.tagName === 'INPUT') {
            // Pattern HTML
            if (field.pattern) {
                try {
                    const re = new RegExp('^(?:' + field.pattern + ')$', 'u');
                    if (!re.test(value)) error = 'Formato inválido.';
                } catch (_) { /* regex ruim, ignora */ }
            }
            // Number min/max
            if (!error && field.type === 'number') {
                const num = parseFloat(value);
                if (isNaN(num)) error = 'Número inválido.';
                else if (field.min !== '' && num < parseFloat(field.min)) error = `Mínimo: ${field.min}.`;
                else if (field.max !== '' && num > parseFloat(field.max)) error = `Máximo: ${field.max}.`;
            }
            // Date validation
            if (!error && (field.type === 'date' || field.type === 'datetime-local') && !field.value) {
                error = 'Data inválida.';
            }
        }
    }

    renderFieldError(group, field, error);
    return error === null;
}

function renderFieldError(group, field, error) {
    const existing = group.querySelector('.field-error');
    if (error) {
        field.classList.add('is-invalid');
        field.classList.remove('is-valid');
        field.setAttribute('aria-invalid', 'true');
        if (existing) {
            existing.querySelector('.error-msg')?.replaceChildren(document.createTextNode(error))
                || (existing.innerHTML = '<i class="fas fa-exclamation-circle" aria-hidden="true"></i> <span class="error-msg">' + error + '</span>');
        } else {
            const el = document.createElement('small');
            el.className = 'field-error';
            el.setAttribute('role', 'alert');
            el.id = field.id + '-error';
            el.innerHTML = '<i class="fas fa-exclamation-circle" aria-hidden="true"></i> <span class="error-msg">' + error + '</span>';
            // Insere após o input/wrapper, antes do hint
            const hint = group.querySelector('.field-hint');
            if (hint) group.insertBefore(el, hint);
            else group.appendChild(el);
        }
    } else {
        field.classList.remove('is-invalid');
        if (field.value.trim()) field.classList.add('is-valid');
        else field.classList.remove('is-valid');
        field.removeAttribute('aria-invalid');
        if (existing) existing.remove();
    }
}

function initInlineValidation() {
    document.querySelectorAll('form.crud-form .form-input, form.crud-form .form-select').forEach((field) => {
        field.addEventListener('blur', () => validateField(field));
        // Limpa erro assim que o usuário começa a digitar de novo
        field.addEventListener('input', () => {
            if (field.classList.contains('is-invalid')) {
                const group = field.closest('.form-group');
                const err = group?.querySelector('.field-error');
                if (err) err.remove();
                field.classList.remove('is-invalid');
                field.removeAttribute('aria-invalid');
            }
            updateCharCounter(field);
        });

        // Adiciona contador de chars se houver maxlength
        if (field.maxLength > 0 && field.maxLength < 1000) {
            const counter = document.createElement('small');
            counter.className = 'char-counter';
            counter.setAttribute('aria-live', 'polite');
            field.closest('.form-group')?.appendChild(counter);
            updateCharCounter(field);
        }
    });
}

function updateCharCounter(field) {
    if (!field.maxLength || field.maxLength <= 0 || field.maxLength >= 1000) return;
    const group = field.closest('.form-group');
    const counter = group?.querySelector('.char-counter');
    if (!counter) return;
    const len = field.value.length;
    const max = field.maxLength;
    counter.textContent = `${len} / ${max}`;
    counter.classList.toggle('is-warning', len > max * 0.8 && len < max);
    counter.classList.toggle('is-danger', len >= max);
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
    restoreSidebarState();
    initSidebarBackdrop();
    initCommandPalette();
    initFormLoading();
    initInlineValidation();
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

    // Spotlight tracking: --mouse-x / --mouse-y movem o gradient radial do stat-card
    document.querySelectorAll('.stat-card').forEach((card) => {
        card.addEventListener('mousemove', (e) => {
            const rect = card.getBoundingClientRect();
            const x = ((e.clientX - rect.left) / rect.width) * 100;
            const y = ((e.clientY - rect.top) / rect.height) * 100;
            card.style.setProperty('--mouse-x', `${x}%`);
            card.style.setProperty('--mouse-y', `${y}%`);
        });
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
