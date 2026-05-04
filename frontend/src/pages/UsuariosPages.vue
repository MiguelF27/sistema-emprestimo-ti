<template>
  <div class="q-pa-md">
    <h4>Funcionários</h4>

    <q-table title="Lista de Funcionários" :rows="usuarios" :columns="columns"
      :filter="{ texto: filter, status: statusFiltro }" :filter-method="customFilter" row-key="id" binary-state-sort>

      <template v-slot:top>

        <div class="row q-gutter-sm items-center">
          <q-input v-model="filter" placeholder="Buscar..." dense outlined debounce="300">

            <template v-slot:append><q-icon name="search" /></template>

          </q-input>
        </div>
      </template>


      <template v-slot:body-cell-acoes="props">
        <q-td :props="props" class="q-gutter-sm">
          <q-btn v-if="$q.dark.isActive" icon="edit" color="white" flat dense round @click="editarUsuario(props.row)" />
          <q-btn v-else icon="edit" color="black" flat dense round @click="editarUsuario(props.row)" />
          <q-btn icon="delete" color="negative" flat dense round @click="deletarUsuario(props.row.id)" />
        </q-td>
      </template>
    </q-table>
    <q-btn color="primary" icon="add" label="Novo" @click="novoUsuario()"></q-btn>

    <q-dialog v-model="dialog" persistent>
      <q-card>
        <q-card-section>
          <div class="text-h6">Cadastro de Funcionário</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-input v-model="form.nome" label="Nome" />
          <q-input v-model="form.email" label="Email" />
          <q-input v-model="form.departamento" label="Departamento" />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn label="Salvar" color="primary" @click="salvarUsuario()"></q-btn>
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

const columns = [
  { name: 'id', label: 'ID', field: 'id', align: 'center' },
  { name: 'nome', label: 'Nome', field: 'nome', align: 'center' },
  { name: 'email', label: 'Email', field: 'email', align: 'center' },
  { name: 'departamento', label: 'Departamento', field: 'departamento', align: 'center' },
  { name: 'acoes', label: 'Ações', field: 'acoes', align: 'center' }
]

const usuarios = ref([])

const statusFiltro = ref('')

const filter = ref('')

const dialog = ref(false)

const editando = ref(false)
const idEditando = ref(null)

const form = ref({
  nome: '',
  email: '',
  departamento: ''
})

onMounted(buscarUsuarios)

async function buscarUsuarios (filtros = {}) {
  try {
    const params = new URLSearchParams()

    if (filtros.nome && filtros.nome.trim() !== '') {
      params.append('nome', filtros.nome)
    }

    if (filtros.email && filtros.email.trim() !== '') {
      params.append('email', filtros.email)
    }

    const url = `/usuarios?${params.toString()}`
    console.log('URL:', url)

    const response = await api.get(url)

    usuarios.value = response.data

  } catch (error) {
    console.error('Erro ao buscar funcionário:', error)
  }
}

function novoUsuario () {
  form.value = {
    nome: '',
    email: '',
    departamento: ''
  }

  editando.value = false
  idEditando.value = null
  dialog.value = true
}

async function salvarUsuario () {

  if (!form.value.nome || !form.value.email || !form.value.departamento) {
    alert('Preencha os campos obrigatórios')
    return
  }

  try {
    if (editando.value) {
      await api.put(`/usuarios/${idEditando.value}`, form.value)

      Notify.create({
        type: 'positive',
        message: 'Funcionário atualizado com sucesso'
      })

    } else {
      await api.post('/usuarios', form.value)

      Notify.create({
        type: 'positive',
        message: 'Funcionário cadastrado com sucesso'
      })
    }

    dialog.value = false

    form.value = {
      nome: '',
      email: '',
      departamento: ''
    }

    editando.value = false
    idEditando.value = null

    await buscarUsuarios()

  } catch (error) {
    console.error('Erro:', error)

    Notify.create({
      type: 'negative',
      message: 'Erro: Email ja cadastrado'
    })
  }
}

const customFilter = (rows, terms) => {
  const search = terms.texto.toLowerCase()

  return rows.filter(row => {

    const matchTexto =
      (row.nome?.toLowerCase().includes(search)) ||
      (row.email?.toLowerCase().includes(search)) ||
      (row.departamento?.toLowerCase().includes(search))

    return matchTexto
  })
}

async function deletarUsuario (id) {
  try {
    await api.delete(`/usuarios/${id}`)
    await buscarUsuarios()

    Notify.create({
      type: 'positive',
      message: 'Funcionário deletado'
    })

  } catch (error) {
    console.error('Erro ao deletar:', error)
    Notify.create({
      type: 'negative',
      message: 'Erro ao deletar funcionário'
    })
  }
}

function editarUsuario (usuario) {
  form.value = {
    nome: usuario.nome,
    email: usuario.email,
    departamento: usuario.departamento
  }

  idEditando.value = usuario.id
  editando.value = true
  dialog.value = true
}

</script>
