---
layout: doc
layoutClass: m-nav-layout
sidebar: false
prev: false
next: false
---

<script setup>
import {NAV_LIST} from '../.vitepress/theme/navlinks/data'
</script>

# 网址导航

<NavLinks v-for="{title, items} in NAV_LIST" :title="title" :items="items"/>
