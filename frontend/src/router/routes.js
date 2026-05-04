const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/IndexPage.vue') },
    { path: 'equipamentos', component: () => import('pages/EquipamentosPages.vue') },
    { path: 'usuarios', component: () => import('pages/UsuariosPages.vue') },
    { path: 'emprestimos', component: () => import('pages/EmprestimosPages.vue') }
    ]
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },

]

export default routes
