<template>
    <div>
        <div>
            <uploadComponent
                @reload:pattern = "reloadPattern"
                :componentState = "componentState"
            />
        </div>
        <div>
            <patternComponent
                :pattern-info   = "patternInfo"
                :componentState = "componentState"
                @reload:upload  = "reloadUpload"
            />
        </div>
    </div>
</template>

<script>

import uploadComponent from './components/upload/index'
import patternComponent from './components/pattern/index'

export default {
    name : 'PattonDictinary_PattonUpload'
    
    , components : {
        uploadComponent
        , patternComponent
    }

    , data (){
        return {
            patternInfo : {}
            , refreshKey : true
            , componentState: true
        }
    }

    , methods : {
        reloadPattern(pattern){
            this.patternInfo = pattern
            this.componentState = !this.componentState
        }

        , reloadUpload(pattern){
            this.componentState = !this.componentState
        }
    }
}
</script>