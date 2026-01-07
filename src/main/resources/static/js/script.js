/* --- ARQUIVO: script.js --- */

// URL BASE do seu Backend Spring Boot
// Certifique-se de habilitar o @CrossOrigin no seu Controller Java se rodar em portas diferentes
const API_URL = 'http://localhost:8081';
// --- ESTADO DA APLICAÇÃO ---
const state = {
    isAuthenticated: false,
    currentView: 'dashboard',
    isMobileMenuOpen: false,
    loading: false,
    data: {
        authors: [],
        books: [],
        stats: {
            totalLivros: 0,
            totalAutores: 0,
            emprestimosAtivos: 0, // Mockado, pois não vi endpoint para isso ainda
            usuariosAtivos: 0     // Mockado
        }
    },
    forms: {
        showBookForm: false,
        showAuthorForm: false
    }
};

// --- FUNÇÕES AUXILIARES DE API ---

/**
 * Função genérica para realizar requisições ao backend
 * Adiciona automaticamente o Token JWT se existir
 */
async function apiFetch(endpoint, options = {}) {
    const token = localStorage.getItem('library_token_jwt');
    
    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    };

    try {
        const response = await fetch(`${API_URL}${endpoint}`, {
            ...options,
            headers: { ...headers, ...options.headers }
        });

        // Tratamento de Sessão Expirada
        if (response.status === 401 || response.status === 403) {
            console.warn("Sessão expirada ou não autorizada.");
            handleLogout(); 
            throw new Error('Acesso negado');
        }

        return response;
    } catch (error) {
            console.error("Erro na requisição:", error);

            // Só mostra alerta de servidor OFF se NÃO for erro de acesso
            if (error.message !== 'Acesso negado') {
                alert("Erro de comunicação com o servidor. Verifique se o Backend está rodando.");
            }
            throw error;
        }
}

// --- AÇÕES DE DADOS (FETCH & POST) ---

async function fetchInitialData() {
    state.loading = true;
    renderApp(); // Re-renderiza para mostrar loadings se tiver

    try {
        // Busca paralela de Livros e Autores
        const [booksRes, authorsRes] = await Promise.all([
            apiFetch('/livros'),
            apiFetch('/autores')
        ]);

        if (booksRes.ok && authorsRes.ok) {
            const books = await booksRes.json();
            const authors = await authorsRes.json();

            // Atualiza Estado
            state.data.books = books;
            state.data.authors = authors;
            
            // Atualiza Estatísticas
            state.data.stats.totalLivros = books.length;
            state.data.stats.totalAutores = authors.length;
            // Para usuarios e emprestimos, manteremos fixo até ter endpoint
        }
    } catch (e) {
        console.error("Falha ao carregar dados iniciais", e);
    } finally {
        state.loading = false;
        renderApp();
    }
}

// --- HANDLERS ---

async function handleLogin(e) {
    e.preventDefault();
    const btn = e.target.querySelector('button');
    const originalText = btn.innerText;
    
    btn.innerText = 'Autenticando...';
    btn.disabled = true;

    const login = document.getElementById('login-user').value;
    const senha = document.getElementById('login-pass').value;

    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login, senha })
        });

        if (response.ok) {
            // 👇 AQUI ESTÁ A CORREÇÃO MÁGICA 👇
            // Lemos o corpo da resposta como JSON, pois seu Java retorna: {"token": "eyJ..."}
            const body = await response.json();
            let token = body.token;

            console.log("Token recebido do Backend:", token); // Vai aparecer no console

            if (token) {
                // Se vier com prefixo, limpamos (apenas garantia)
                if (token.startsWith('Bearer ')) {
                    token = token.slice(7);
                }

                localStorage.setItem('library_token_jwt', token);
                state.isAuthenticated = true;
                fetchInitialData(); // Carrega os dados
            } else {
                alert("Erro: O servidor respondeu OK, mas não enviou o token no JSON.");
                btn.disabled = false;
                btn.innerText = originalText;
            }
        } else {
            alert("Credenciais inválidas!");
            btn.disabled = false;
            btn.innerText = originalText;
        }
    } catch (error) {
        console.error(error);
        alert("Erro de comunicação com o servidor.");
        btn.disabled = false;
        btn.innerText = originalText;
    }
}

function handleLogout() {
    state.isAuthenticated = false;
    localStorage.removeItem('library_token_jwt');
    state.currentView = 'dashboard';
    renderApp();
}

function navigate(view) {
    state.currentView = view;
    state.isMobileMenuOpen = false;
    state.forms.showBookForm = false;
    state.forms.showAuthorForm = false;
    renderApp();
}

function toggleMobileMenu() {
    state.isMobileMenuOpen = !state.isMobileMenuOpen;
    renderApp();
}

function toggleForm(type) {
    if(type === 'book') state.forms.showBookForm = !state.forms.showBookForm;
    if(type === 'author') state.forms.showAuthorForm = !state.forms.showAuthorForm;
    renderApp();
}

async function handleAddBook(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    
    // Objeto conforme esperado pelo seu DTO Java (CadastroLivroDTO ou Livro)
    const newBook = {
        titulo: formData.get('titulo'),
        isbn: formData.get('isbn'),
        dataPublicacao: formData.get('data'), // Verifique o formato de data esperado pelo Java (YYYY-MM-DD)
        genero: formData.get('genero'),
        idAutor: formData.get('autorId') // Assumindo que seu endpoint espera o ID para vincular
    };

    try {
        const response = await apiFetch('/livros', {
            method: 'POST',
            body: JSON.stringify(newBook)
        });

        if (response.ok) {
            alert('Livro cadastrado com sucesso!');
            toggleForm('book');
            fetchInitialData(); // Recarrega lista
        } else {
            const err = await response.json();
            alert('Erro ao salvar: ' + (err.message || response.statusText));
        }
    } catch (error) {
        console.error(error);
    }
}

async function handleAddAuthor(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    
    // Objeto conforme DTO (AutorDTO)
    const newAuthor = {
        nome: formData.get('nome'),
        nacionalidade: formData.get('nacionalidade'),
        dataNascimento: formData.get('nascimento')
    };

    try {
        const response = await apiFetch('/autores', {
            method: 'POST',
            body: JSON.stringify(newAuthor)
        });

        if (response.ok) {
            alert('Autor cadastrado com sucesso!');
            toggleForm('author');
            fetchInitialData(); // Recarrega lista
        } else {
            alert('Erro ao salvar autor.');
        }
    } catch (error) {
        console.error(error);
    }
}

// --- FUNÇÕES DE RENDERIZAÇÃO (VIEW) ---
// Mantive a estrutura visual idêntica, apenas lendo de state.data

function renderLogin() {
    return `
        <div class="min-h-screen w-full bg-slate-50 flex items-center justify-center p-4">
            <div class="bg-white rounded-xl shadow-sm border border-slate-200 w-full max-w-md p-8 fade-in">
                <div class="text-center mb-8">
                    <div class="w-12 h-12 bg-indigo-600 rounded-xl flex items-center justify-center mx-auto mb-4">
                        <i data-lucide="library" class="text-white w-6 h-6"></i>
                    </div>
                    <h1 class="text-2xl font-bold text-slate-900">Library API</h1>
                    <p class="text-slate-500 mt-2">Sistema Integrado</p>
                </div>

                <form onsubmit="handleLogin(event)">
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-slate-700 mb-1">Login</label>
                        <input type="text" id="login-user" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all" required>
                    </div>
                    <div class="mb-6">
                        <label class="block text-sm font-medium text-slate-700 mb-1">Senha</label>
                        <input type="password" id="login-pass" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all" required>
                    </div>
                    
                    <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors flex items-center justify-center gap-2">
                        Acessar Sistema
                    </button>
                </form>
            </div>
        </div>
    `;
}

function renderSidebar() {
    const getLinkClass = (view) => state.currentView === view 
        ? 'bg-indigo-50 text-indigo-600 font-semibold' 
        : 'text-slate-600 hover:bg-slate-50';

    return `
        <aside class="hidden md:flex w-64 bg-white border-r border-slate-200 flex-col fixed h-full z-10">
            <div class="p-6 border-b border-slate-100">
                <div class="flex items-center gap-3">
                    <div class="bg-indigo-600 p-2 rounded-lg">
                        <i data-lucide="library" class="text-white w-5 h-5"></i>
                    </div>
                    <span class="font-bold text-xl text-slate-800">Library API</span>
                </div>
            </div>
            
            <nav class="flex-1 p-4 space-y-2">
                <button onclick="navigate('dashboard')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${getLinkClass('dashboard')}">
                    <i data-lucide="layout-dashboard" class="w-5 h-5"></i>
                    <span>Dashboard</span>
                </button>
                <button onclick="navigate('books')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${getLinkClass('books')}">
                    <i data-lucide="book" class="w-5 h-5"></i>
                    <span>Livros</span>
                </button>
                <button onclick="navigate('authors')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${getLinkClass('authors')}">
                    <i data-lucide="users" class="w-5 h-5"></i>
                    <span>Autores</span>
                </button>
                <button onclick="navigate('users')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${getLinkClass('users')}">
                    <i data-lucide="user" class="w-5 h-5"></i>
                    <span>Usuários</span>
                </button>
            </nav>

            <div class="p-4 border-t border-slate-100">
                <button onclick="handleLogout()" class="w-full flex items-center gap-3 px-4 py-3 text-red-600 hover:bg-red-50 rounded-lg transition-colors">
                    <i data-lucide="log-out" class="w-5 h-5"></i>
                    <span>Sair</span>
                </button>
            </div>
        </aside>
    `;
}

function renderMobileHeader() {
    return `
        <div class="md:hidden fixed w-full bg-white border-b border-slate-200 z-20 px-4 py-3 flex items-center justify-between">
            <div class="flex items-center gap-2">
                <div class="bg-indigo-600 p-1.5 rounded-lg">
                    <i data-lucide="library" class="text-white w-4 h-4"></i>
                </div>
                <span class="font-bold text-lg text-slate-800">Library</span>
            </div>
            <button onclick="toggleMobileMenu()">
                <i data-lucide="${state.isMobileMenuOpen ? 'x' : 'menu'}" class="w-6 h-6 text-slate-600"></i>
            </button>
        </div>
    `;
}

function renderMobileMenu() {
    if (!state.isMobileMenuOpen) return '';
    return `
        <div class="fixed inset-0 bg-white z-10 pt-20 px-4 pb-4 md:hidden fade-in">
            <nav class="space-y-2">
                <button onclick="navigate('dashboard')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-50">
                    <i data-lucide="layout-dashboard"></i> Dashboard
                </button>
                <button onclick="navigate('books')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-50">
                    <i data-lucide="book"></i> Livros
                </button>
                <button onclick="navigate('authors')" class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-50">
                    <i data-lucide="users"></i> Autores
                </button>
                <button onclick="handleLogout()" class="w-full flex items-center gap-3 px-4 py-3 text-red-600">
                    <i data-lucide="log-out"></i> Sair
                </button>
            </nav>
        </div>
    `;
}

function renderDashboardView() {
    return `
        <div class="space-y-6 fade-in">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <div class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 flex items-center gap-4">
                    <div class="bg-blue-500 p-3 rounded-lg text-white"><i data-lucide="book"></i></div>
                    <div><p class="text-sm text-slate-500">Total de Livros</p><p class="text-2xl font-bold text-slate-800">${state.data.stats.totalLivros}</p></div>
                </div>
                <div class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 flex items-center gap-4">
                    <div class="bg-emerald-500 p-3 rounded-lg text-white"><i data-lucide="users"></i></div>
                    <div><p class="text-sm text-slate-500">Autores</p><p class="text-2xl font-bold text-slate-800">${state.data.stats.totalAutores}</p></div>
                </div>
                <div class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 flex items-center gap-4">
                    <div class="bg-violet-500 p-3 rounded-lg text-white"><i data-lucide="user"></i></div>
                    <div><p class="text-sm text-slate-500">Usuários Ativos</p><p class="text-2xl font-bold text-slate-800">${state.data.stats.usuariosAtivos}</p></div>
                </div>
                <div class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 flex items-center gap-4">
                    <div class="bg-amber-500 p-3 rounded-lg text-white"><i data-lucide="library"></i></div>
                    <div><p class="text-sm text-slate-500">Empréstimos</p><p class="text-2xl font-bold text-slate-800">${state.data.stats.emprestimosAtivos}</p></div>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <div class="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
                    <h3 class="font-bold text-slate-800 mb-4">Últimos Livros Adicionados</h3>
                    <div class="space-y-4">
                        ${state.data.books.length > 0 ? state.data.books.slice(-5).map(book => `
                            <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg hover:bg-slate-100 transition-colors">
                                <div class="flex items-center gap-3">
                                    <div class="w-10 h-10 bg-indigo-100 rounded flex items-center justify-center text-indigo-600 font-bold">
                                        ${book.titulo ? book.titulo.charAt(0) : '?'}
                                    </div>
                                    <div>
                                        <p class="font-medium text-slate-900">${book.titulo}</p>
                                        <p class="text-xs text-slate-500">ISBN: ${book.isbn}</p>
                                    </div>
                                </div>
                                <span class="text-xs bg-white border border-slate-200 px-2 py-1 rounded text-slate-600">${book.genero || 'Geral'}</span>
                            </div>
                        `).join('') : '<p class="text-slate-400 text-sm">Nenhum livro encontrado.</p>'}
                    </div>
                </div>

                <div class="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
                    <h3 class="font-bold text-slate-800 mb-4">Status do Sistema</h3>
                    <div class="p-4 bg-green-50 text-green-700 rounded-lg border border-green-200 text-sm">
                        <p><strong>Conectado ao Backend:</strong> ${API_URL}</p>
                        <p class="mt-1">Autenticação JWT ativa.</p>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function renderBooksView() {
    const formHTML = state.forms.showBookForm ? `
        <div class="bg-white p-6 rounded-xl shadow-sm border border-indigo-100 mb-6 fade-in">
            <h3 class="font-bold text-indigo-900 mb-4">Cadastrar Novo Livro</h3>
            <form onsubmit="handleAddBook(event)" class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Título</label>
                    <input type="text" name="titulo" required class="w-full px-3 py-2 border border-slate-300 rounded-lg">
                </div>
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">ISBN</label>
                    <input type="text" name="isbn" class="w-full px-3 py-2 border border-slate-300 rounded-lg">
                </div>
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Data Publicação</label>
                    <input type="date" name="data" class="w-full px-3 py-2 border border-slate-300 rounded-lg">
                </div>
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Gênero</label>
                    <select name="genero" class="w-full px-3 py-2 border border-slate-300 rounded-lg bg-white">
                        <option value="ROMANCE">Romance</option>
                        <option value="FANTASIA">Fantasia</option>
                        <option value="FICCAO">Ficção</option>
                        <option value="MISTERIO">Mistério</option>
                        <option value="BIOGRAFIA">Biografia</option>
                    </select>
                </div>
                <div class="md:col-span-2">
                     <label class="block text-sm font-medium text-slate-700 mb-1">Autor</label>
                     <select name="autorId" class="w-full px-3 py-2 border border-slate-300 rounded-lg bg-white" required>
                        <option value="">Selecione...</option>
                        ${state.data.authors.map(a => `<option value="${a.id}">${a.nome}</option>`).join('')}
                     </select>
                </div>
                <div class="md:col-span-2 flex justify-end gap-2 mt-4">
                     <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-indigo-700">Salvar Livro</button>
                </div>
            </form>
        </div>
    ` : '';

    return `
        <div class="space-y-6 fade-in">
            <div class="flex justify-between items-center">
                <h2 class="text-2xl font-bold text-slate-800">Gerenciar Livros</h2>
                <button onclick="toggleForm('book')" class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors flex items-center gap-2">
                    ${state.forms.showBookForm ? 'Cancelar' : '<i data-lucide="plus" class="w-4 h-4"></i> Novo Livro'}
                </button>
            </div>

            ${formHTML}

            <div class="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="w-full text-left text-sm text-slate-600">
                        <thead class="bg-slate-50 border-b border-slate-200">
                            <tr>
                                <th class="px-6 py-3 font-semibold text-slate-700">ID</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Título</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">ISBN</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Gênero</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Ações</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            ${state.data.books.map(book => `
                                <tr class="hover:bg-slate-50">
                                    <td class="px-6 py-4">#${book.id}</td>
                                    <td class="px-6 py-4 font-medium text-slate-900">${book.titulo}</td>
                                    <td class="px-6 py-4 font-mono text-xs"><span class="bg-slate-100 px-2 py-1 rounded">${book.isbn}</span></td>
                                    <td class="px-6 py-4"><span class="bg-indigo-50 text-indigo-700 px-2 py-1 rounded-full text-xs font-semibold">${book.genero}</span></td>
                                    <td class="px-6 py-4">
                                        <button class="text-slate-400 hover:text-indigo-600 font-medium text-sm">Editar</button>
                                    </td>
                                </tr>
                            `).join('')}
                            ${state.data.books.length === 0 ? '<tr><td colspan="5" class="px-6 py-8 text-center text-slate-400">Nenhum livro cadastrado.</td></tr>' : ''}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;
}

function renderAuthorsView() {
    const formHTML = state.forms.showAuthorForm ? `
        <div class="bg-white p-6 rounded-xl shadow-sm border border-emerald-100 mb-6 fade-in">
            <h3 class="font-bold text-emerald-900 mb-4">Cadastrar Novo Autor</h3>
            <form onsubmit="handleAddAuthor(event)" class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Nome Completo</label>
                    <input type="text" name="nome" required class="w-full px-3 py-2 border border-slate-300 rounded-lg">
                </div>
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Nacionalidade</label>
                    <input type="text" name="nacionalidade" class="w-full px-3 py-2 border border-slate-300 rounded-lg">
                </div>
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-1">Nascimento</label>
                    <input type="date" name="nascimento" class="w-full px-3 py-2 border border-slate-300 rounded-lg">
                </div>
                <div class="md:col-span-3 flex justify-end">
                     <button type="submit" class="bg-emerald-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-emerald-700">Salvar Autor</button>
                </div>
            </form>
        </div>
    ` : '';

    return `
         <div class="space-y-6 fade-in">
            <div class="flex justify-between items-center">
                <h2 class="text-2xl font-bold text-slate-800">Gerenciar Autores</h2>
                <button onclick="toggleForm('author')" class="bg-emerald-600 hover:bg-emerald-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors flex items-center gap-2">
                    ${state.forms.showAuthorForm ? 'Cancelar' : '<i data-lucide="plus" class="w-4 h-4"></i> Novo Autor'}
                </button>
            </div>

            ${formHTML}

            <div class="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="w-full text-left text-sm text-slate-600">
                        <thead class="bg-slate-50 border-b border-slate-200">
                            <tr>
                                <th class="px-6 py-3 font-semibold text-slate-700">ID</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Nome</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Nacionalidade</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Nascimento</th>
                                <th class="px-6 py-3 font-semibold text-slate-700">Ações</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            ${state.data.authors.map(author => `
                                <tr class="hover:bg-slate-50">
                                    <td class="px-6 py-4">#${author.id}</td>
                                    <td class="px-6 py-4 font-medium text-slate-900">${author.nome}</td>
                                    <td class="px-6 py-4">${author.nacionalidade}</td>
                                    <td class="px-6 py-4 text-slate-500">${author.dataNascimento}</td>
                                    <td class="px-6 py-4">
                                        <button class="text-slate-400 hover:text-emerald-600 font-medium text-sm">Detalhes</button>
                                    </td>
                                </tr>
                            `).join('')}
                            ${state.data.authors.length === 0 ? '<tr><td colspan="5" class="px-6 py-8 text-center text-slate-400">Nenhum autor cadastrado.</td></tr>' : ''}
                        </tbody>
                    </table>
                </div>
            </div>
         </div>
    `;
}

function renderApp() {
    const app = document.getElementById('app');
    app.innerHTML = '';

    if (!state.isAuthenticated) {
        app.innerHTML = renderLogin();
    } else {
        let contentHTML = '';
        
        if (state.currentView === 'dashboard') contentHTML = renderDashboardView();
        else if (state.currentView === 'books') contentHTML = renderBooksView();
        else if (state.currentView === 'authors') contentHTML = renderAuthorsView();
        else if (state.currentView === 'users') {
            contentHTML = `
                <div class="flex flex-col items-center justify-center h-96 text-slate-400 bg-white rounded-xl border border-slate-200 border-dashed fade-in">
                    <i data-lucide="settings" class="w-12 h-12 mb-4 text-slate-300"></i>
                    <p>Gestão de Usuários e Clients em desenvolvimento...</p>
                </div>
            `;
        }

        const mainLayout = `
            ${renderSidebar()}
            ${renderMobileHeader()}
            ${renderMobileMenu()}
            
            <main class="flex-1 md:ml-64 p-4 md:p-8 pt-20 md:pt-8 overflow-y-auto bg-slate-50 h-screen">
                 <header class="flex justify-between items-center mb-8 fade-in">
                    <div>
                        <h1 class="text-2xl font-bold text-slate-800 capitalize">${state.currentView === 'dashboard' ? 'Dashboard' : state.currentView}</h1>
                        <p class="text-slate-500 text-sm">Gerencie seu sistema de biblioteca</p>
                    </div>
                    <div class="flex items-center gap-4">
                        <div class="hidden md:flex items-center gap-2 bg-white px-3 py-1.5 rounded-full border border-slate-200 shadow-sm">
                            <div class="w-2 h-2 rounded-full ${state.loading ? 'bg-yellow-500' : 'bg-green-500'} animate-pulse"></div>
                            <span class="text-xs font-medium text-slate-600">${state.loading ? 'Carregando...' : 'Sistema Online'}</span>
                        </div>
                        <div class="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center text-indigo-700 font-bold border-2 border-white shadow-sm">
                            AD
                        </div>
                    </div>
                </header>
                ${contentHTML}
            </main>
        `;
        app.innerHTML = mainLayout;
    }

    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }
}

// --- INICIALIZAÇÃO ---

function init() {
    const token = localStorage.getItem('library_token_jwt');
    if(token) {
        state.isAuthenticated = true;
        fetchInitialData();
    }
    renderApp();
}

document.addEventListener('DOMContentLoaded', () => {
    init();
});