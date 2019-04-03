(ns weather.core
  (:require [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [reagent.core :as r :refer [atom]]
            [applied-science.js-interop :as j]
            [ajax.core :refer [GET]]
            [day8.re-frame/http-fx]
            [weather.handlers]
            [weather.subs]
            [weather.common.ui :refer [text view os expo platform keyboard-avoiding-view img-bg]]
            [weather.components.search-input :refer [search-input]]
            [weather.utils :refer [image-source-map]]))
;
; (def testmap {:ios {:name "apple"}
;               :android {:name "android is for average"}})
; (def mytest (js->clj (j/call platform :select (clj->js testmap)) :keywordize-keys true))

(declare styles)

(defn app-root []
  (let [location (subscribe [:city-name])
        app-db (subscribe [:app-db])]
    (r/create-class
      {:display-name "weather-view-component"
       ; :component-did-mount
       ; (fn []
       ;   (dispatch [:fetch-data @(subscribe [:city-name])]))
       :reagent-render
       (fn []
         [keyboard-avoiding-view {:style (:container styles) :behavior "padding"}
          ; (println platform)
          ; (println "platform.select test: " (:name mytest))
          ; (println "OS === " os)
          ; (println "text style: " (:text-style styles))
          [img-bg {:source (:clear image-source-map)
                   :style (:image-container styles)
                   :image-style (:image styles)}
           [view {:style (:details-container styles)}
            ; (println "view update")
            (j/call js/console :log "Location Value: " @location)
            [text {:style (merge (:large-text styles) (:text-style styles))} @location]
            [text {:style (merge (:small-text styles) (:text-style styles))} "Light Cloud"]
            [text {:style (merge (:large-text styles) (:text-style styles))} "25\u00B0"]
            [search-input "Search any city"]
            (j/call js/console :log "APP-DB: " @app-db)]]])})))

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch-sync [:set-initial-city "Chittagong"])
  (j/call expo :registerRootComponent (r/reactify-component app-root)))


(def font-family #js {:ios #js {:font-family "AvenirNext-Regular"}
                      :android #js {:font-family "Roboto"}})

(def styles {:container {:flex 1 :background-color "#34495E"}
             :text-style (assoc (js->clj (j/call platform :select font-family) :keywordize-keys true) :text-align "center" :color "white")
                          ; :font-family (if (= os "android") "Roboto" "AvenirNext-Regular")}
             :large-text {:font-size 44}
             :small-text {:font-size 18}
             :image-container {:flex 1}
             :image {:flex 1 :width nil :height nil :resize-mode "cover"}
             :details-container {:flex 1 :justify-content "center" :background-color "rgba(0,0,0,0.1)" :padding-horizontal 20}})