import { useEffect, useMemo, useState } from 'react'

const emptyFood = { nome: '', quantidade: '', unidade: 'kg', dataValidade: '', origem: '', status: 'DISPONIVEL' }
const emptyInstitution = { nome: '', documento: '', email: '', telefone: '', rua: '', numero: '', cidade: '', estado: 'PR' }

const navItems = [
  { id: 'dashboard', label: 'Visão geral', icon: '⌂' },
  { id: 'alimentos', label: 'Alimentos', icon: '◒' },
  { id: 'instituicoes', label: 'Instituições', icon: '⌁' },
  { id: 'solicitacoes', label: 'Solicitações', icon: '↗' },
  { id: 'entregas', label: 'Pessoas atendidas', icon: '✓' }
]

const statusLabels = {
  PENDENTE: 'Pendente',
  APROVADA: 'Aprovada',
  RECUSADA: 'Recusada',
  ATENDIDA: 'Atendida',
  CANCELADA: 'Cancelada',
  DISPONIVEL: 'Disponível',
  INDISPONIVEL: 'Indisponível'
}

const statusTone = {
  PENDENTE: 'amber', APROVADA: 'lime', RECUSADA: 'rose', ATENDIDA: 'teal', CANCELADA: 'slate',
  DISPONIVEL: 'lime', INDISPONIVEL: 'slate'
}

function api(path, options = {}) {
  return fetch(path, { headers: { 'Content-Type': 'application/json', ...(options.headers || {}) }, ...options })
    .then(async (response) => {
      const body = response.status === 204 ? null : await response.json().catch(() => null)
      if (!response.ok) throw new Error(body?.message || 'Não foi possível concluir esta ação.')
      return body
    })
}

function Icon({ name }) {
  const paths = {
    search: <><circle cx="11" cy="11" r="6.5" /><path d="m16 16 4 4" /></>,
    plus: <><path d="M12 5v14M5 12h14" /></>,
    arrow: <><path d="M5 12h13M13 7l5 5-5 5" /></>,
    close: <><path d="m6 6 12 12M18 6 6 18" /></>,
    chart: <><path d="M4 19V5M4 19h16" /><path d="m7 15 3-4 3 2 5-6" /></>,
    calendar: <><rect x="4" y="5" width="16" height="15" rx="2" /><path d="M8 3v4M16 3v4M4 10h16" /></>,
    pin: <><path d="M12 21s6-5.2 6-11a6 6 0 1 0-12 0c0 5.8 6 11 6 11Z" /><circle cx="12" cy="10" r="2" /></>,
    box: <><path d="m4 7 8-4 8 4v10l-8 4-8-4Z" /><path d="m4 7 8 4 8-4M12 11v10" /></>,
    building: <><path d="M4 21V5l8-3 8 3v16M2 21h20M8 9h1M15 9h1M8 13h1M15 13h1M8 17h1M15 17h1" /></>,
    logout: <><path d="M10 5H5v14h5M14 8l4 4-4 4M18 12H9" /></>,
    bell: <><path d="M18 9a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4" /></>
  }
  return <svg className="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">{paths[name]}</svg>
}

function App() {
  const [page, setPage] = useState('dashboard')
  const [foods, setFoods] = useState([])
  const [institutions, setInstitutions] = useState([])
  const [requests, setRequests] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState(null)
  const [modal, setModal] = useState(null)
  const [foodForm, setFoodForm] = useState(emptyFood)
  const [institutionForm, setInstitutionForm] = useState(emptyInstitution)
  const [requestForm, setRequestForm] = useState({ instituicaoId: '', destinatarioNome: '', destinatarioDocumento: '', destinatarioRua: '', destinatarioCidade: '', destinatarioEstado: 'PR', motivo: '', observacao: '', itens: [{ alimentoId: '', quantidadeSolicitada: '', unidade: 'kg' }] })
  const [search, setSearch] = useState('')
  const [foodStatus, setFoodStatus] = useState('TODOS')
  const [requestStatus, setRequestStatus] = useState('TODOS')

  const loadData = async () => {
    setLoading(true); setError('')
    try {
      const [foodData, institutionData, requestData] = await Promise.all([
        api('/api/alimentos'), api('/api/instituicoes'), api('/api/solicitacoes')
      ])
      setFoods(foodData || []); setInstitutions(institutionData || []); setRequests(requestData || [])
    } catch (err) { setError(err.message) } finally { setLoading(false) }
  }

  useEffect(() => { loadData() }, [])
  useEffect(() => { if (toast) { const timer = setTimeout(() => setToast(null), 3600); return () => clearTimeout(timer) } }, [toast])

  const availableFoods = foods.filter((food) => food.status === 'DISPONIVEL')
  const filteredFoods = foods.filter((food) => {
    const term = search.toLowerCase()
    return (!term || [food.nome, food.origem, food.unidade].some((value) => value?.toLowerCase().includes(term))) && (foodStatus === 'TODOS' || food.status === foodStatus)
  })
  const filteredRequests = requests.filter((request) => {
    const term = search.toLowerCase()
    return (!term || request.id?.toLowerCase().includes(term) || request.observacao?.toLowerCase().includes(term)) && (requestStatus === 'TODOS' || request.status === requestStatus)
  })
  const expiringFoods = foods.filter((food) => food.dataValidade && new Date(`${food.dataValidade}T23:59:59`) - new Date() < 1000 * 60 * 60 * 24 * 7 && food.status === 'DISPONIVEL')
  const openRequests = requests.filter((request) => ['PENDENTE', 'APROVADA'].includes(request.status))
  const institutionName = (id) => institutions.find((item) => item.id === id)?.nome || 'Instituição não identificada'
  const foodName = (id) => foods.find((item) => item.id === id)?.nome || 'Alimento removido'

  const stats = [
    { label: 'Alimentos disponíveis', value: availableFoods.length, detail: `${foods.length} cadastrados`, tone: 'lime', icon: 'box' },
    { label: 'Instituições parceiras', value: institutions.length, detail: 'receptoras ativas', tone: 'teal', icon: 'building' },
    { label: 'Solicitações em aberto', value: openRequests.length, detail: `${requests.length} no total`, tone: 'amber', icon: 'arrow' }
  ]

  const notify = (message, type = 'success') => setToast({ message, type })
  const closeModal = () => { setModal(null); setFoodForm(emptyFood); setInstitutionForm(emptyInstitution); setRequestForm({ instituicaoId: '', destinatarioNome: '', destinatarioDocumento: '', destinatarioRua: '', destinatarioCidade: '', destinatarioEstado: 'PR', motivo: '', observacao: '', itens: [{ alimentoId: '', quantidadeSolicitada: '', unidade: 'kg' }] }) }
  const editFood = (food) => { setFoodForm({ ...food }); setModal('food-edit') }
  const editInstitution = (institution) => { setInstitutionForm({ id: institution.id, nome: institution.nome, documento: institution.documento, email: institution.email, telefone: institution.telefone, rua: institution.endereco?.rua || '', numero: institution.endereco?.numero || '', cidade: institution.endereco?.cidade || '', estado: institution.endereco?.estado || 'PR' }); setModal('institution-edit') }

  async function createFood(event) {
    event.preventDefault()
    try { const payload = { ...foodForm, quantidade: Number(foodForm.quantidade) }; const path = foodForm.id ? `/api/alimentos/${foodForm.id}` : '/api/alimentos'; const method = foodForm.id ? 'PUT' : 'POST'; await api(path, { method, body: JSON.stringify(payload) }); notify(foodForm.id ? 'Alimento atualizado.' : 'Alimento adicionado ao estoque.'); closeModal(); loadData() }
    catch (err) { notify(err.message, 'error') }
  }
  async function createInstitution(event) {
    event.preventDefault()
    try {
      const payload = { nome: institutionForm.nome, documento: institutionForm.documento, email: institutionForm.email, telefone: institutionForm.telefone, endereco: { rua: institutionForm.rua, numero: institutionForm.numero, cidade: institutionForm.cidade, estado: institutionForm.estado } }
      const path = institutionForm.id ? `/api/instituicoes/${institutionForm.id}` : '/api/instituicoes'; const method = institutionForm.id ? 'PUT' : 'POST'; await api(path, { method, body: JSON.stringify(payload) }); notify(institutionForm.id ? 'Instituição atualizada.' : 'Instituição cadastrada com sucesso.'); closeModal(); loadData()
    } catch (err) { notify(err.message, 'error') }
  }
  async function createRequest(event) {
    event.preventDefault()
    try {
      const payload = { instituicaoId: requestForm.instituicaoId, destinatarioNome: requestForm.destinatarioNome, destinatarioDocumento: requestForm.destinatarioDocumento, destinatarioRua: requestForm.destinatarioRua, destinatarioCidade: requestForm.destinatarioCidade, destinatarioEstado: requestForm.destinatarioEstado, motivo: requestForm.motivo, observacao: requestForm.observacao, itens: requestForm.itens.map((item) => ({ alimentoId: item.alimentoId, quantidadeSolicitada: Number(item.quantidadeSolicitada), unidade: item.unidade })) }
      await api('/api/solicitacoes', { method: 'POST', body: JSON.stringify(payload) }); notify('Solicitação criada e enviada para análise.'); closeModal(); loadData()
    } catch (err) { notify(err.message, 'error') }
  }
  async function toggleFood(food) {
    try { await api(`/api/alimentos/${food.id}`, { method: 'PUT', body: JSON.stringify({ ...food, status: food.status === 'DISPONIVEL' ? 'INDISPONIVEL' : 'DISPONIVEL' }) }); notify(`Alimento marcado como ${food.status === 'DISPONIVEL' ? 'indisponível' : 'disponível'}.`); loadData() }
    catch (err) { notify(err.message, 'error') }
  }
  async function changeStatus(id, status) {
    try { await api(`/api/solicitacoes/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) }); notify(`Solicitação marcada como ${statusLabels[status].toLowerCase()}.`); loadData() }
    catch (err) { notify(err.message, 'error') }
  }

  const pageTitle = { dashboard: ['Bom dia, equipe', 'Transforme excedentes em impacto local.'], alimentos: ['Alimentos', 'Tudo que pode chegar mais longe, em um só lugar.'], instituicoes: ['Instituições', 'Parceiros que fazem a redistribuição acontecer.'], solicitacoes: ['Solicitações', 'Acompanhe cada pedido até a entrega.'], entregas: ['Pessoas atendidas', 'Histórico de quem já recebeu alimentos pela operação.'] }[page]

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark"><span /><span /></div><div><strong>recolhe<span>+</span></strong><small>gestão responsável</small></div></div>
      <div className="sidebar-section-label">Operação</div>
      <nav className="main-nav">{navItems.map((item) => <button key={item.id} className={page === item.id ? 'nav-item active' : 'nav-item'} onClick={() => { setPage(item.id); setSearch('') }}><span className="nav-icon">{item.icon}</span>{item.label}{item.id === 'solicitacoes' && openRequests.length > 0 && <b>{openRequests.length}</b>}</button>)}</nav>
      <div className="sidebar-bottom"><div className="impact-card"><span className="impact-dot" /> <div><strong>ODS 12 em ação</strong><p>Menos descarte.<br />Mais cuidado.</p></div></div><div className="profile"><div className="avatar">ES</div><div><strong>Equipe Recolhe+</strong><small>Operação local</small></div><button className="icon-button" aria-label="Sair"><Icon name="logout" /></button></div></div>
    </aside>

    <main className="main-content">
      <header className="topbar"><div className="mobile-brand"><div className="brand-mark"><span /><span /></div><strong>recolhe<span>+</span></strong></div><div className="topbar-context"><span className="live-dot" /> API conectada <i /> <span>Atualizado agora</span></div><div className="topbar-actions"><button className="icon-button notification" aria-label="Notificações"><Icon name="bell" /><span /></button><div className="topbar-date"><span>Hoje</span><strong>{new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short' }).format(new Date())}</strong></div></div></header>
      <section className="page-heading"><div><p className="eyebrow">CENTRAL DE REDISTRIBUIÇÃO <span>•</span> MARINGÁ, PR</p><h1>{pageTitle[0]}</h1><p className="heading-subtitle">{pageTitle[1]}</p></div><div className="heading-actions">{page === 'dashboard' && <button className="button primary" onClick={() => setModal('request')}><Icon name="plus" /> Criar solicitação</button>}</div></section>

      {error && <div className="connection-alert"><div><strong>Não foi possível carregar os dados</strong><span>{error} Verifique se o backend Spring Boot está rodando na porta 8080.</span></div><button className="button small" onClick={loadData}>Tentar novamente</button></div>}
      {loading && <div className="loading-bar"><span /></div>}
      {!loading && page === 'dashboard' && <Dashboard stats={stats} foods={foods} requests={requests} expiringFoods={expiringFoods} institutionName={institutionName} onNavigate={setPage} onNewRequest={() => setModal('request')} />}
      {!loading && page === 'alimentos' && <FoodsPage foods={filteredFoods} search={search} setSearch={setSearch} status={foodStatus} setStatus={setFoodStatus} onNew={() => setModal('food')} onEdit={editFood} onToggle={toggleFood} />}
      {!loading && page === 'instituicoes' && <InstitutionsPage institutions={institutions} search={search} setSearch={setSearch} onNew={() => setModal('institution')} onEdit={editInstitution} />}
      {!loading && page === 'solicitacoes' && <RequestsPage requests={filteredRequests} search={search} setSearch={setSearch} status={requestStatus} setStatus={setRequestStatus} institutionName={institutionName} foodName={foodName} onNew={() => setModal('request')} onStatus={changeStatus} />}{!loading && page === 'entregas' && <DeliveriesPage requests={requests.filter((request) => request.status === 'ATENDIDA')} foodName={foodName} />}
    </main>

    {(modal === 'food' || modal === 'food-edit') && <FoodModal form={foodForm} setForm={setFoodForm} editing={modal === 'food-edit'} onClose={closeModal} onSubmit={createFood} />}
    {(modal === 'institution' || modal === 'institution-edit') && <InstitutionModal form={institutionForm} setForm={setInstitutionForm} editing={modal === 'institution-edit'} onClose={closeModal} onSubmit={createInstitution} />}
    {modal === 'request' && <RequestModal form={requestForm} setForm={setRequestForm} foods={availableFoods} institutions={institutions} onClose={closeModal} onSubmit={createRequest} />}
    {toast && <div className={`toast ${toast.type}`}><span>{toast.type === 'success' ? '✓' : '!'}</span>{toast.message}</div>}
  </div>
}

function Dashboard({ stats, foods, requests, expiringFoods, institutionName, onNavigate, onNewRequest }) {
  return <section className="content-grid dashboard-grid"><div className="stats-grid">{stats.map((stat) => <div className={`stat-card ${stat.tone}`} key={stat.label}><div className="stat-top"><span>{stat.label}</span><div className="stat-icon"><Icon name={stat.icon} /></div></div><strong>{stat.value.toString().padStart(2, '0')}</strong><small>{stat.detail}</small></div>)}</div><div className="dashboard-main"><div className="panel spotlight"><div className="panel-heading"><div><p className="eyebrow">NORTE DA OPERAÇÃO</p><h2>O que sobra pode<br /><em>chegar mais longe.</em></h2></div><span className="spotlight-number">12<span>º ODS</span></span></div><p className="spotlight-copy">Organize alimentos, conecte instituições e acompanhe cada redistribuição com clareza.</p><button className="text-button" onClick={() => onNavigate('solicitacoes')}>Ver solicitações <Icon name="arrow" /></button><div className="spotlight-orbit orbit-one" /><div className="spotlight-orbit orbit-two" /><div className="spotlight-stem" /></div><div className="panel recent-panel"><div className="panel-heading compact"><div><p className="eyebrow">MOVIMENTO RECENTE</p><h2>Últimas solicitações</h2></div><button className="ghost-link" onClick={() => onNavigate('solicitacoes')}>Ver todas <Icon name="arrow" /></button></div>{requests.length === 0 ? <EmptyState title="Nenhuma solicitação ainda" copy="O próximo pedido aparecerá aqui." /> : <div className="activity-list">{requests.slice(0, 4).map((request) => <div className="activity-row" key={request.id}><div className="activity-mark"><Icon name="arrow" /></div><div className="activity-info"><strong>{request.destinatarioNome || 'Destinatário não informado'}</strong><span>{request.itens?.length || 0} item(ns) · {request.dataSolicitacao || 'Hoje'}</span></div><StatusBadge status={request.status} /></div>)}</div>}</div></div><aside className="dashboard-side"><div className="panel expiry-panel"><div className="panel-heading compact"><div><p className="eyebrow">ATENÇÃO</p><h2>Validade próxima</h2></div><span className="count-badge">{expiringFoods.length}</span></div>{expiringFoods.length === 0 ? <EmptyState title="Tudo sob controle" copy="Nenhum alimento vence nos próximos 7 dias." /> : <div className="expiry-list">{expiringFoods.slice(0, 3).map((food) => <div className="expiry-item" key={food.id}><div className="food-avatar">{food.nome?.slice(0, 1)}</div><div><strong>{food.nome}</strong><span>{food.quantidade} {food.unidade} · vence em {formatDate(food.dataValidade)}</span></div></div>)}</div>}</div><div className="panel quick-panel"><div className="quick-symbol">↗</div><div><p className="eyebrow">ATALHO RÁPIDO</p><h2>Tem excedente?</h2><p>Registre em menos de um minuto.</p></div><button className="button primary full" onClick={() => onNavigate('alimentos')}>Adicionar alimento <Icon name="arrow" /></button></div></aside></section>
}

function FoodsPage({ foods, search, setSearch, status, setStatus, onNew, onEdit, onToggle }) {
  return <section className="content-grid"><Toolbar search={search} setSearch={setSearch} placeholder="Buscar por nome ou origem" actionLabel="Novo alimento" onAction={onNew} showAction={foods.length > 0} /><div className="data-panel"><div className="filter-row"><div className="filter-label">STATUS DO ESTOQUE</div>{['TODOS', 'DISPONIVEL', 'INDISPONIVEL'].map((item) => <button key={item} className={status === item ? 'filter-pill active' : 'filter-pill'} onClick={() => setStatus(item)}>{item === 'TODOS' ? 'Todos' : statusLabels[item]}</button>)}</div>{foods.length === 0 ? <EmptyState title="Nenhum alimento encontrado" copy="Cadastre um excedente para começar a organizar o estoque." action={<button className="button primary" onClick={onNew}><Icon name="plus" /> Cadastrar alimento</button>} /> : <div className="table-wrap"><table><thead><tr><th>ALIMENTO</th><th>QUANTIDADE</th><th>ORIGEM</th><th>VALIDADE</th><th>STATUS</th><th>AÇÕES</th></tr></thead><tbody>{foods.map((food) => <tr key={food.id}><td><div className="table-name"><div className="food-avatar">{food.nome?.slice(0, 1)}</div><div><strong>{food.nome}</strong><span>ID {food.id?.slice(-8)}</span></div></div></td><td><strong>{food.quantidade} {food.unidade}</strong></td><td>{food.origem}</td><td><span className={isExpiring(food.dataValidade) ? 'date-warning' : ''}><Icon name="calendar" /> {formatDate(food.dataValidade)}</span></td><td><StatusBadge status={food.status} /></td><td><div className="row-actions"><button className="mini-action" onClick={() => onToggle(food)}>{food.status === 'DISPONIVEL' ? 'Indisponível' : 'Disponível'}</button><button className="mini-action edit" onClick={() => onEdit(food)}>Editar</button></div></td></tr>)}</tbody></table></div>}</div></section>
}

function InstitutionsPage({ institutions, search, setSearch, onNew, onEdit }) {
  const visible = institutions.filter((item) => !search || [item.nome, item.documento, item.email, item.endereco?.cidade].some((value) => value?.toLowerCase().includes(search.toLowerCase())))
  return <section className="content-grid"><Toolbar search={search} setSearch={setSearch} placeholder="Buscar instituição ou cidade" actionLabel="Nova instituição" onAction={onNew} showAction={visible.length > 0} /><div className="institution-grid">{visible.length === 0 ? <div className="data-panel full-span"><EmptyState title="Nenhuma instituição cadastrada" copy="Cadastre o primeiro parceiro receptor para criar uma solicitação." action={<button className="button primary" onClick={onNew}><Icon name="plus" /> Cadastrar instituição</button>} /></div> : visible.map((institution) => <article className="institution-card" key={institution.id}><div className="institution-card-top"><div className="institution-logo">{institution.nome?.slice(0, 2).toUpperCase()}</div><StatusBadge status="APROVADA" label="Parceira" /></div><h3>{institution.nome}</h3><p className="institution-document">{institution.documento}</p><span className="institution-id">ID {institution.id}</span><div className="institution-meta"><span><Icon name="pin" /> {institution.endereco?.cidade} · {institution.endereco?.estado}</span><span>{institution.email}</span></div><div className="card-footer"><span>Instituição receptora</span><button className="mini-action edit" onClick={() => onEdit(institution)}>Editar</button></div></article>)}</div></section>
}

function DeliveriesPage({ requests, foodName }) {
  return <section className="content-grid"><div className="data-panel">{requests.length === 0 ? <EmptyState title="Nenhuma entrega concluída" copy="Quando uma solicitação for marcada como atendida, ela aparecerá aqui." /> : <div className="delivery-list">{requests.map((request) => <article className="delivery-card" key={request.id}><div className="delivery-head"><div><p className="eyebrow">ENTREGA CONCLUÍDA · {formatDate(request.dataEntrega || request.dataSolicitacao)}</p><h2>{request.destinatarioNome || 'Destinatário não informado'}</h2><p>{request.destinatarioDocumento || 'Documento não informado'}</p></div><StatusBadge status="ATENDIDA" /></div><div className="delivery-details"><span><strong>Produtos</strong>{request.itens?.map((item) => `${item.quantidadeSolicitada} ${item.unidade} ${foodName(item.alimentoId)}`).join(', ')}</span><span><strong>Endereço</strong>{request.destinatarioRua || '—'}, {request.destinatarioCidade || '—'} · {request.destinatarioEstado || '—'}</span><span><strong>Motivo</strong>{request.motivo || request.observacao || '—'}</span></div></article>)}</div>}</div></section>
}

function RequestsPage({ requests, search, setSearch, status, setStatus, institutionName, foodName, onNew, onStatus }) {
  return <section className="content-grid"><Toolbar search={search} setSearch={setSearch} placeholder="Buscar solicitação" actionLabel="Nova solicitação" onAction={onNew} showAction={requests.length > 0} /><div className="filter-row request-filters"><div className="filter-label">ACOMPANHAMENTO</div>{['TODOS', 'PENDENTE', 'APROVADA', 'ATENDIDA', 'RECUSADA'].map((item) => <button key={item} className={status === item ? 'filter-pill active' : 'filter-pill'} onClick={() => setStatus(item)}>{item === 'TODOS' ? 'Todas' : statusLabels[item]}</button>)}</div><div className="request-list">{requests.length === 0 ? <div className="data-panel"><EmptyState title="Nenhuma solicitação encontrada" copy="Crie um pedido para uma instituição receptora." action={<button className="button primary" onClick={onNew}><Icon name="plus" /> Criar solicitação</button>} /></div> : requests.map((request) => <article className="request-card" key={request.id}><div className="request-card-main"><div className="request-id"><span className="request-symbol">↗</span><div><span>Solicitação #{request.id?.slice(-6)}</span><strong>{institutionName(request.instituicaoId)}</strong></div></div><StatusBadge status={request.status} /><div className="request-date"><Icon name="calendar" /> {formatDate(request.dataSolicitacao)}</div><div className="request-items">{request.itens?.slice(0, 2).map((item) => <span key={item.alimentoId}>{item.quantidadeSolicitada} {item.unidade} {foodName(item.alimentoId)}</span>)}{request.itens?.length > 2 && <span>+ {request.itens.length - 2} itens</span>}</div></div><div className="request-card-action">{request.status === 'PENDENTE' && <><button className="button approve" onClick={() => onStatus(request.id, 'APROVADA')}>Aprovar</button><button className="button reject" onClick={() => onStatus(request.id, 'RECUSADA')}>Recusar</button></>}{request.status === 'APROVADA' && <button className="button approve" onClick={() => onStatus(request.id, 'ATENDIDA')}>Marcar atendida <Icon name="arrow" /></button>}{['ATENDIDA', 'RECUSADA', 'CANCELADA'].includes(request.status) && <span className="closed-label">Fluxo encerrado</span>}</div></article>)}</div></section>
}

function Toolbar({ search, setSearch, placeholder, actionLabel, onAction, showAction = true }) { return <div className="toolbar"><div className="search-box"><Icon name="search" /><input value={search} onChange={(event) => setSearch(event.target.value)} placeholder={placeholder} /></div>{showAction && <button className="button primary" onClick={onAction}><Icon name="plus" /> {actionLabel}</button>}</div> }
function StatusBadge({ status, label }) { return <span className={`status-badge ${statusTone[status] || 'slate'}`}><i /> {label || statusLabels[status] || status}</span> }
function EmptyState({ title, copy, action }) { return <div className="empty-state"><div className="empty-icon">◌</div><h3>{title}</h3><p>{copy}</p>{action}</div> }
function formatDate(date) { if (!date) return 'Sem data'; return new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' }).format(new Date(`${date}T12:00:00`)).replace('.', '') }
function isExpiring(date) { return date && new Date(`${date}T23:59:59`) - new Date() < 1000 * 60 * 60 * 24 * 7 }

function Modal({ title, kicker, children, onClose, onSubmit, submitLabel }) { return <div className="modal-backdrop" onMouseDown={(event) => event.target === event.currentTarget && onClose()}><div className="modal"><div className="modal-header"><div><p className="eyebrow">{kicker}</p><h2>{title}</h2></div><button className="icon-button" onClick={onClose}><Icon name="close" /></button></div><form onSubmit={onSubmit}><div className="modal-body">{children}</div><div className="modal-footer"><button type="button" className="button ghost" onClick={onClose}>Cancelar</button><button type="submit" className="button primary">{submitLabel}<Icon name="arrow" /></button></div></form></div></div> }
function Field({ label, children, wide = false }) { return <label className={wide ? 'field wide' : 'field'}><span>{label}</span>{children}</label> }
function FoodModal({ form, setForm, editing, onClose, onSubmit }) {
  const update = (key, value) => setForm({ ...form, [key]: value })
  return <Modal title={editing ? 'Editar alimento' : 'Adicionar alimento'} kicker={editing ? 'ATUALIZAR ESTOQUE' : 'NOVO EXCEDENTE'} onClose={onClose} onSubmit={onSubmit} submitLabel={editing ? 'Salvar alterações' : 'Salvar alimento'}><div className="form-grid"><Field label="Nome do alimento" wide><input required value={form.nome} onChange={(e) => update('nome', e.target.value)} placeholder="Ex.: Arroz integral" /></Field><Field label="Quantidade"><input required min="0.1" step="0.1" type="number" value={form.quantidade} onChange={(e) => update('quantidade', e.target.value)} placeholder="0" /></Field><Field label="Unidade"><select value={form.unidade} onChange={(e) => update('unidade', e.target.value)}><option>kg</option><option>un</option><option>litros</option><option>caixas</option></select></Field><Field label="Data de validade"><input required type="date" value={form.dataValidade} onChange={(e) => update('dataValidade', e.target.value)} /></Field><Field label="Origem" wide><input required value={form.origem} onChange={(e) => update('origem', e.target.value)} placeholder="Ex.: Supermercado Esperança" /></Field><Field label="Disponibilidade" wide><select value={form.status} onChange={(e) => update('status', e.target.value)}><option value="DISPONIVEL">Disponível</option><option value="INDISPONIVEL">Indisponível</option></select></Field></div></Modal>
}

function InstitutionModal({ form, setForm, editing, onClose, onSubmit }) {
  const update = (key, value) => setForm({ ...form, [key]: value })
  return <Modal title={editing ? 'Editar instituição' : 'Nova instituição'} kicker="PARCEIRO RECEPTOR" onClose={onClose} onSubmit={onSubmit} submitLabel={editing ? 'Salvar alterações' : 'Salvar instituição'}><div className="form-grid"><Field label="Nome da instituição" wide><input required value={form.nome} onChange={(e) => update('nome', e.target.value)} placeholder="Ex.: Instituto Esperança" /></Field><Field label="Documento (CPF/CNPJ)"><input required value={form.documento} onChange={(e) => update('documento', e.target.value)} placeholder="CNPJ ou documento" /></Field><Field label="Telefone"><input required value={form.telefone} onChange={(e) => update('telefone', e.target.value)} placeholder="(44) 99999-9999" /></Field><Field label="E-mail" wide><input required type="email" value={form.email} onChange={(e) => update('email', e.target.value)} placeholder="contato@instituicao.org" /></Field><div className="form-divider wide">Endereço da instituição</div><Field label="Rua"><input required value={form.rua} onChange={(e) => update('rua', e.target.value)} /></Field><Field label="Número"><input required value={form.numero} onChange={(e) => update('numero', e.target.value)} /></Field><Field label="Cidade"><input required value={form.cidade} onChange={(e) => update('cidade', e.target.value)} /></Field><Field label="Estado"><input required maxLength="2" value={form.estado} onChange={(e) => update('estado', e.target.value.toUpperCase())} /></Field></div></Modal>
}

function RequestModal({ form, setForm, foods, institutions, onClose, onSubmit }) {
  const updateItem = (index, changes) => setForm((current) => ({ ...current, itens: current.itens.map((item, itemIndex) => itemIndex === index ? { ...item, ...changes } : item) }))
  const selectedFood = (id) => foods.find((food) => food.id === id)
  const selectInstitution = (id) => setForm((current) => ({ ...current, instituicaoId: id }))
  return <Modal title="Criar solicitação" kicker="NOVA REDISTRIBUIÇÃO" onClose={onClose} onSubmit={onSubmit} submitLabel="Enviar solicitação"><div className="form-grid"><Field label="Instituição doadora" wide><select required value={form.instituicaoId} onChange={(e) => selectInstitution(e.target.value)}><option value="">Selecione a instituição doadora</option>{institutions.map((institution) => <option value={institution.id} key={institution.id}>{institution.nome}</option>)}</select></Field><p className="form-help wide">Selecione a instituição, empresa ou estabelecimento que está doando ou disponibilizando o alimento. Os dados de quem receberá a entrega são preenchidos separadamente abaixo.</p><Field label="Nome da pessoa/instituição atendida" wide><input required value={form.destinatarioNome} onChange={(e) => setForm({ ...form, destinatarioNome: e.target.value })} placeholder="Quem receberá a entrega?" /></Field><Field label="CPF/CNPJ"><input required value={form.destinatarioDocumento} onChange={(e) => setForm({ ...form, destinatarioDocumento: e.target.value })} /></Field><Field label="Motivo da entrega"><input required value={form.motivo} onChange={(e) => setForm({ ...form, motivo: e.target.value })} placeholder="Ex.: Apoio alimentar" /></Field><Field label="Rua e número"><input required value={form.destinatarioRua} onChange={(e) => setForm({ ...form, destinatarioRua: e.target.value })} /></Field><Field label="Cidade"><input required value={form.destinatarioCidade} onChange={(e) => setForm({ ...form, destinatarioCidade: e.target.value })} /></Field><Field label="Estado"><input required maxLength="2" value={form.destinatarioEstado} onChange={(e) => setForm({ ...form, destinatarioEstado: e.target.value.toUpperCase() })} /></Field><div className="form-divider wide">Itens solicitados</div>{form.itens.map((item, index) => <div className="request-item-form wide" key={index}><select required value={item.alimentoId} onChange={(e) => { const food = selectedFood(e.target.value); updateItem(index, { alimentoId: e.target.value, ...(food ? { unidade: food.unidade } : {}) }) }}><option value="">Selecione o alimento</option>{foods.map((food) => <option value={food.id} key={food.id}>{food.nome} · {food.quantidade} {food.unidade}</option>)}</select><input required min="0.1" step="0.1" type="number" value={item.quantidadeSolicitada} onChange={(e) => updateItem(index, { quantidadeSolicitada: e.target.value })} placeholder="Qtd." /><select value={item.unidade} onChange={(e) => updateItem(index, { unidade: e.target.value })}><option>kg</option><option>un</option><option>litros</option><option>caixas</option></select>{form.itens.length > 1 && <button type="button" className="remove-item" onClick={() => setForm((current) => ({ ...current, itens: current.itens.filter((_, itemIndex) => itemIndex !== index) }))}><Icon name="close" /></button>}</div>)}<button type="button" className="add-item" onClick={() => setForm((current) => ({ ...current, itens: [...current.itens, { alimentoId: '', quantidadeSolicitada: '', unidade: 'kg' }] }))}><Icon name="plus" /> Adicionar outro item</button></div></Modal>
}

export default App
