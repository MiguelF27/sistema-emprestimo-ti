<template>
  <div class="q-pa-md">
    <h4>Empréstimos</h4>

    <q-table title="Lista de Empréstimos" :rows="emprestimos" :columns="columns"
      :filter="{ texto: filter, status: statusFiltro }" :filter-method="customFilter" row-key="id">

      <template v-slot:top>

        <div class="row q-gutter-sm items-center">
          <q-input v-model="filter" placeholder="Buscar..." dense outlined debounce="300">

            <template v-slot:append><q-icon name="search" /></template>

          </q-input>
          <q-select v-model="statusFiltro" :options="[
            { label: 'Todos', value: '' },
            { label: 'Ativo', value: 'ATIVO' },
            { label: 'Finalizado', value: 'FINALIZADO' },
            { label: 'Atrasado', value: 'ATRASADO' }
          ]" label="Status" dense outlined emit-value map-options />
        </div>
      </template>

      <template v-slot:body-cell-acoes="props">
        <q-td :props="props" class="q-gutter-sm">
        <q-btn
          v-if="props.row.status !== 'FINALIZADO'"
          icon="assignment_return"
          :color="$q.dark.isActive ? 'white' : 'black'"
          flat dense round
          @click="prepararDevolucao(props.row.id)"
        />
        <q-btn icon="delete" color="negative" flat dense round @click="deletarEmprestimo(props.row.id)" />
        </q-td>
      </template>
    </q-table>
    <q-btn color="primary" icon="add" label="Novo" @click="dialog = true"></q-btn>

    <q-dialog v-model="dialog" @hide="limparFormulario">
      <q-card>
        <q-card-section>
          <div class="text-h6">Cadastro de Empréstimos</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-select v-model="form.usuarioId" :options="usuariosOptions" label="Funcionário" emit-value map-options />
          <q-select v-model="form.equipamentoId" :options="equipamentosOptions" label="Equipamento" emit-value
            map-options />
          <q-input v-model="form.dataDevolucaoPrevista" label="Data Prevista para Devolução" type="date" />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn label="Salvar" color="primary" @click="criarEmprestimo()"></q-btn>
          <q-btn flat label="Fechar" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="dialogDevolucao" persistent>
      <q-card style="min-width: 350px">
        <q-card-section class="row items-center">
          <div class="text-h6">Finalizar Empréstimo</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section class="q-pt-none text-body1">
          Como está o estado deste equipamento ao retornar?
        </q-card-section>

        <q-card-actions align="center" class="q-pb-md">
          <q-btn
            label="Disponível"
            color="positive"
            icon="check_circle"
            @click="confirmarDevolucao('DISPONIVEL')"
          />
          <q-btn
            label="Manutenção"
            color="warning"
            icon="build"
            @click="confirmarDevolucao('MANUTENCAO')"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from 'boot/axios'
import { Notify } from 'quasar'
import { formatarStatus } from 'src/enums/status'

const emprestimos = ref([])

const dialog = ref(false)

const usuarios = ref([])
const equipamentos = ref([])

const usuariosOptions = ref([])
const equipamentosOptions = ref([])

const statusFiltro = ref('')

const filter = ref('')

const dialogDevolucao = ref(false)
const emprestimoSelecionadoId = ref(null)


const columns = [
  {
    name: 'usuario',
    label: 'Usuário',
    field: row => row.usuario?.nome,
    align: 'center'
  },
  {
    name: 'equipamento',
    label: 'Equipamento',
    field: row => row.equipamento?.nome,
    align: 'center'
  },
  {
    name: 'dataEmprestimo',
    label: 'Data Empréstimo',
    field: 'dataEmprestimo',
    align: 'center',
    format: (val) => {
      if (!val) return '';
      const data = new Date(val);

      return data.toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    }

  },
  {
    name: 'dataPrevista',
    label: 'Devolução Prevista',
    field: 'dataDevolucaoPrevista',
    align: 'center',
    format: (val) => {
      if (!val) return '';
      const data = new Date(val);

      return data.toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    }
  },
  {
    name: 'status',
    label: 'Status',
    field: row => formatarStatus(row.status),
    align: 'center'
  },
  {
    name: 'diasAtraso',
    label: 'Dias de Atraso',
    field: 'diasAtraso',
    align: 'center'
  },
  {
    name: 'acoes',
    label: 'Ações',
    field: 'acoes',
    align: 'center'
  },
]

const form = ref({
  usuarioId: null,
  equipamentoId: null,
  dataDevolucaoPrevista: ''
})

onMounted(async () => {
  await buscarEmprestimos()
  await buscarUsuarios()
  await buscarEquipamentos()
})

async function buscarUsuarios () {
  try {
    const response = await api.get('/usuarios')

    usuarios.value = response.data

    usuariosOptions.value = usuarios.value.map(u => ({
      label: `${u.nome} (Email: ${u.email})`,
      value: u.id
    }))
  } catch (error) {
    console.error('Erro ao buscar funcionários:', error)
  }
}

async function buscarEquipamentos () {
  const response = await api.get('/equipamentos')
  equipamentos.value = response.data

  equipamentosOptions.value = equipamentos.value.map(e => {

    const isDisponivel = e.status === 'DISPONIVEL'

    let avisoStatus = ''
    if (e.status === 'EM_USO') avisoStatus = ' - [EM USO]'
    if (e.status === 'MANUTENCAO') avisoStatus = ' - [EM MANUTENÇÃO]'

    return {
      label: `${e.nome} (Código: ${e.patrimonio})${avisoStatus}`,
      value: e.id,
      disable: !isDisponivel
    }
  })
}

async function buscarEmprestimos (filtros = {}) {
  try {
    const params = new URLSearchParams()

    if (filtros.nome && filtros.nome.trim() !== '') {
      params.append('nome', filtros.nome)
    }

    if (filtros.status && filtros.status.trim() !== '') {
      params.append('status', filtros.status)
    }

    const url = `/emprestimos?${params.toString()}`
    console.log('URL:', url)

    const response = await api.get(url)

    emprestimos.value = response.data

  } catch (error) {
    console.error('Erro ao buscar empréstimos:', error)
  }
}

function limparFormulario() {
  form.value = {
    usuarioId: null,
    equipamentoId: null,
    dataDevolucaoPrevista: ''
  }
}

async function criarEmprestimo () {

  if (!form.value.usuarioId || !form.value.equipamentoId) {
    alert('Preencha os campos obrigatórios')
    return
  }

  try {
    await api.post('/emprestimos', form.value)

    Notify.create({
      type: 'positive',
      message: 'Emprestimo cadastrado com sucesso'
    })

    dialog.value = false

    form.value = {
      usuarioId: null,
      equipamentoId: null,
      dataDevolucaoPrevista: ''
    }

    await buscarEmprestimos()

  } catch (error) {
    const mensagem = error.response?.data?.message || 'Equipamento indisponível'

    Notify.create({
      type: 'negative',
      message: mensagem
    })
  }
}

const customFilter = (rows, terms) => {
  const search = terms.texto.toLowerCase()
  const status = terms.status

  return rows.filter(row => {

    const matchTexto =
      (row.usuario?.nome?.toLowerCase().includes(search)) ||
      (row.equipamento?.nome?.toLowerCase().includes(search))

    const matchStatus = !status || row.status === status

    return matchTexto && matchStatus
  })
}

function prepararDevolucao (id) {
  emprestimoSelecionadoId.value = id
  dialogDevolucao.value = true
}

async function confirmarDevolucao (proximoStatus) {
  try {
    await api.put(`/emprestimos/devolver/${emprestimoSelecionadoId.value}`, null, {
      params: { status: proximoStatus }
    })

    Notify.create({
      type: 'positive',
      message: `Equipamento devolvido e marcado como ${proximoStatus}`
    })

    dialogDevolucao.value = false
    await buscarEmprestimos()
  } catch (error) {
    console.error('Erro na devolução', error)
    Notify.create({ type: 'negative', message: 'Erro ao processar devolução' })
  }
}

async function deletarEmprestimo (id) {
  try {
    await api.delete(`/emprestimos/${id}`)
    await buscarEmprestimos()

    Notify.create({
      type: 'positive',
      message: 'Emprestimo deletado'
    })

  } catch (error) {
    console.error('Erro ao deletar:', error)
    Notify.create({
      type: 'negative',
      message: 'Erro ao deletar emprestimo'
    })
  }
}



</script>
