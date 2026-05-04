export const statusMap = {
  DISPONIVEL: 'Disponível',
  EM_USO: 'Em uso',
  MANUTENCAO: 'Manutenção' ,
  FINALIZADO: 'Finalizado',
  ATRASADO: 'Atrasado',
  ATIVO: 'Ativo'
}

export function formatarStatus(status) {
  return statusMap[status] || status
}
