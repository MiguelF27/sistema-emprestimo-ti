<template>
  <q-page class="flex flex-center column q-pa-md">

    <q-img v-if="$q.dark.isActive" alt="IWT logo" src="~assets/iwtwhite.png"
      style="max-width: 350px; width: 100%; height: 150px" fit="contain" />
    <q-img v-else alt="IWT logo" src="~assets/iwtblack.png" style="max-width: 350px; width: 100%; height: 150px"
      fit="contain" />

  </q-page>
</template>
