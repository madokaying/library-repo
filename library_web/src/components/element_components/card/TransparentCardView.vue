<template>
    <div class="wrapper">
        <div class="transparent-div">
            <h1>md不会！</h1>
        </div>
    </div>
</template>

<script>
    export default {
        name: "TransparentCardView"
    }
</script>

<style scoped>
    .wrapper {
        min-height: 200px;
        min-width: 100%;
        z-index: -100;
        background-image: url("https://r2.touchgal.net/2023/01/cad0a147c5154256.jpg");
        overflow: hidden;
    }
    .transparent-div {
        min-width: 100%;
        z-index: 2;
    }
</style>