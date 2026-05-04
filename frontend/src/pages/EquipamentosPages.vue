<template>
  <div class="q-pa-md">
    <h4>Equipamentos</h4>

    <q-table title="Lista de Equipamentos" :rows="equipamentos" :columns="columns"
      :filter="{ texto: filter, status: statusFiltro }" :filter-method="customFilter" row-key="id">

      <template v-slot:top>

        <div class="row q-gutter-sm items-center">
          <q-input v-model="filter" placeholder="Buscar..." dense outlined debounce="300">

            <template v-slot:append><q-icon name="search" /></template>

          </q-input>
          <q-select v-model="statusFiltro" :options="[
            { label: 'Todos', value: '' },
            { label: 'Disponível', value: 'DISPONIVEL' },
            { label: 'Manutenção', value: 'MANUTENCAO' },
            { label: 'Em uso', value: 'EM_USO' }
          ]" label="Status" dense outlined emit-value map-options />
        </div>
      </template>

      <template v-slot:body-cell-acoes="props">
        <q-td :props="props" class="q-gutter-sm">
          <q-btn v-if="$q.dark.isActive" icon="edit" color="white" flat dense round
            @click="editarEquipamento(props.row)" />
          <q-btn v-else icon="edit" color="black" flat dense round @click="editarEquipamento(props.row)" />
          <q-btn icon="delete" color="negative" flat dense round @click="deletarEquipamento(props.row.id)" />
        </q-td>
      </template>
    </q-table>
    <q-btn color="primary" icon="add" label="Novo" @click="novoEquipamento()"></q-btn>

    <q-dialog v-model="dialog" persistent>
      <q-card>
        <q-card-section>
          <div class="text-h6">Cadastro de Equipamento</div>
        </q-card-section>

        <q-card-section>
          <q-input v-model="form.nome" label="nome" />
          <q-input v-model="form.tipo" label="tipo" />
          <q-input v-model="form.categoria" label="categoria" />
          <q-input v-model="form.patrimonio" label="patrimonio" />
          <q-select
            v-if="editando && form.status !== 'EM_USO'"
            v-model="form.status"
            :options="[
              { label: 'Disponível', value: 'DISPONIVEL' },
              { label: 'Manutenção', value: 'MANUTENCAO' }
            ]"
            label="Status do Equipamento"
            emit-value
            map-options
          />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn label="Salvar" color="primary" @click="salvarEquipamento()"></q-btn>
          <q-btn flat label="Fechar" color="primary" v-close-popup />
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

const dialog = ref(false)

const equipamentos = ref([])

const statusFiltro = ref('')

const filter = ref('')

const editando = ref(false)
const idEditando = ref(null)

const columns = [
  { name: 'id', label: 'ID', field: 'id', align: 'center' },
  { name: 'nome', label: 'Nome', field: 'nome', align: 'center' },
  { name: 'tipo', label: 'Tipo', field: 'tipo', align: 'center' },
  { name: 'categoria', label: 'Categoria', field: 'categoria', align: 'center' },
  { name: 'status', label: 'Status', field: row => formatarStatus(row.status), align: 'center' },
  { name: 'acoes', label: 'Ações', field: 'acoes', align: 'center' }
]

const form = ref({
  nome: '',
  tipo: '',
  categoria: '',
  patrimonio: '',
  status: ''
})

onMounted(buscarEquipamentos)

async function buscarEquipamentos (filtros = {}) {
  try {
    const params = new URLSearchParams()

    if (filtros.nome && filtros.nome.trim() !== '') {
      params.append('nome', filtros.nome)
    }

    if (filtros.status && filtros.status.trim() !== '') {
      params.append('status', filtros.status)
    }

    const url = `/equipamentos?${params.toString()}`
    console.log('URL:', url)

    const response = await api.get(url)

    equipamentos.value = response.data

  } catch (error) {
    console.error('Erro ao buscar equipamentos:', error)
  }
}

function novoEquipamento() {
  form.value = {
    nome: '',
    tipo: '',
    categoria: '',
    patrimonio: ''
  }

  editando.value = false
  idEditando.value = null
  dialog.value = true
}
async function salvarEquipamento () {

  if (!form.value.nome || !form.value.tipo || !form.value.patrimonio) {
    alert('Preencha os campos obrigatórios')
    return
  }

  try {
    if (editando.value) {
      await api.put(`/equipamentos/${idEditando.value}`, form.value)

      Notify.create({
        type: 'positive',
        message: 'Equipamento atualizado com sucesso'
      })

    } else {
      await api.post('/equipamentos', form.value)

      Notify.create({
        type: 'positive',
        message: 'Equipamento cadastrado com sucesso'
      })
    }

    dialog.value = false

    form.value = {
      nome: '',
      tipo: '',
      categoria: '',
      patrimonio: '',
      status: ''
    }

    editando.value = false
    idEditando.value = null

    await buscarEquipamentos()

  } catch (error) {
    console.error('Erro:', error)

    Notify.create({
      type: 'negative',
      message: 'Erro ao salvar equipamento'
    })
  }
}

const customFilter = (rows, terms) => {
  const search = terms.texto.toLowerCase()
  const status = terms.status

  return rows.filter(row => {

    const matchTexto =
      (row.nome?.toLowerCase().includes(search)) ||
      (row.patrimonio?.toLowerCase().includes(search)) ||
      (row.tipo?.toLowerCase().includes(search))

    const matchStatus = !status || row.status === status

    return matchTexto && matchStatus
  })
}

async function deletarEquipamento (id) {
  try {
    await api.delete(`/equipamentos/${id}`)

    await buscarEquipamentos()

    Notify.create({
      type: 'positive',
      message: 'Equipamento deletado'
    })

  } catch (error) {
    console.error('Erro ao deletar:', error)
    Notify.create({
      type: 'negative',
      message: 'Erro ao deletar equipamento'
    })
  }
}

function editarEquipamento (equipamento) {
  form.value = {
    nome: equipamento.nome,
    tipo: equipamento.tipo,
    categoria: equipamento.categoria,
    patrimonio: equipamento.patrimonio,
    status: equipamento.status
  }

  idEditando.value = equipamento.id
  editando.value = true

  dialog.value = true
}

</script>
