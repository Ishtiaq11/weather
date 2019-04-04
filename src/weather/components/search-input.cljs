(ns weather.components.search-input
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [applied-science.js-interop :as j]

            [weather.common.ui :refer [view text-input]]
            [weather.handlers]
            [weather.subs]))


(declare styles)

(defn search-input
  [placeholder]
  (let [txt (atom "")]
    (fn [placeholder]
      [view {:style (:container styles)}
       [text-input {:style (:text-input styles)
                    :auto-correct false
                    :value @txt
                    :placeholder placeholder
                    :placeholder-text-color "white"
                    :underline-color-android "transparent"
                    :clear-button-mode "always"
                    :on-change-text #(reset! txt %)
                    :on-submit-editing #(when (not= (count @txt) 0)
                                          (do
                                            (j/call js/console :log "you want the weather of" @txt "city")
                                            ; (dispatch [:update-city-name @txt])
                                            (dispatch [:fetch-location-id @txt])
                                            (reset! txt "")))}]])))
       ; (println "our txt: " @txt)])))
       ; (let [this (r/current-component)]
       ;   (println "cur comp" this)
       ;   (println "args are" (r/children this))
       ;   (println "props are" (r/props this))
       ;   (println "args type" (type (r/children this))))])))

(def styles {:container {:background-color "#666"
                         :height 40
                         :margin-top 20
                         :margin-horizontal 40
                         :padding-horizontal 10
                         :border-radius 5}
             :text-input {:flex 1
                          :color "white"}})
