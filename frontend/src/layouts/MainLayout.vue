<template>
  <q-layout view="lHh Lpr lFf">

    <q-header elevated :class="$q.dark.isActive ? 'bg-dark text-white' : 'bg-white text-black'">
      <q-toolbar>
        <q-btn flat dense round icon="menu" aria-label="Menu" @click="toggleLeftDrawer" />

        <q-toolbar-title class="text-weight-bold">
          IWT - Gerenciador
        </q-toolbar-title>

        <q-btn flat round dense :icon="$q.dark.isActive ? 'light_mode' : 'dark_mode'" @click="alternarModoNoturno"
          class="q-mr-sm" />
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <div class="q-pa-lg flex flex-center">
        <q-img v-if="$q.dark.isActive" src="~assets/iwtwhite.png" fit="contain"
          style="max-width: 150px; height: 60px" />
        <q-img v-else src="~assets/iwtblack.png" fit="contain" style="max-width: 150px; height: 60px" />
      </div>

      <q-separator />

      <q-list class="q-pt-md">
        <EssentialLink v-for="link in linksList" :key="link.title" v-bind="link" />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref } from 'vue'
import EssentialLink from 'components/EssentialLink.vue'
import { useQuasar } from 'quasar'

const $q = useQuasar()

function alternarModoNoturno () {
  $q.dark.toggle()
}

const linksList = [
  {
    title: 'Funcionários',
    icon: 'people',
    link: '/usuarios',
  },
  {
    title: 'Equipamentos',
    icon: 'laptop',
    link: '/equipamentos',
  },
  {
    title: 'Empréstimos',
    icon: 'assignment_ind',
    link: '/emprestimos',
  },
]

const leftDrawerOpen = ref(false)

function toggleLeftDrawer () {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>
