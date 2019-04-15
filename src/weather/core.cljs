(ns weather.core
  (:require [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [reagent.core :as r :refer [atom]]
            [applied-science.js-interop :as j]
            [clojure.string :as string]
            [day8.re-frame.http-fx]
            [weather.handlers]
            [weather.subs]
            [weather.common.ui :refer [text view os expo platform keyboard-avoiding-view img-bg status-bar activity-indicator]]
            [weather.components.search-input :refer [search-input]]
            [weather.utils :refer [weather-image-map]]))
;
; (def testmap {:ios {:name "apple"}
;               :android {:name "android is for average"}})
; (def mytest (js->clj (j/call platform :select (clj->js testmap)) :keywordize-keys true))

(declare styles)

(defn weather-info
  [city-name weather-state-name temperature]
  [view
   [text {:style (merge (:large-text styles) (:text-style styles))} city-name]
   [text {:style (merge (:small-text styles) (:text-style styles))} weather-state-name]
   [text {:style (merge (:large-text styles) (:text-style styles))} (str (j/call js/Math :round temperature) "\u00B0")]])

(def get-image-for-weather (fn [weather]
                             (when weather
                               (get weather-image-map (keyword (string/replace weather " " ""))))))

(def handle-update-location (fn [city]
                              (and city (> (count city) 0)
                                (dispatch [:fetch-location-id city])
                                (j/call js/console :log "update location: " city))))

(defn app-root []
  (let [city-name (subscribe [:city-name])
        weather-state-name (subscribe [:weather-state-name])
        temperature (subscribe [:temperature])
        app-db (subscribe [:app-db])
        loading? (subscribe [:loading])
        error? (subscribe [:error])]
    (r/create-class
      {:display-name "weather-view-component"
       :component-did-mount
       (fn []
         (handle-update-location @city-name)
         ; (j/call js/console :log "Weather state name: " @weather-state-name)
         (j/call js/console :log "Component did mount for city: " @city-name))
       :reagent-render
       (fn []
         [keyboard-avoiding-view {:style (:container styles) :behavior "padding"}
          ; (println platform)
          ; (println "platform.select test: " (:name mytest))
          ; (println "OS === " os)
          ; (println "text style: " (:text-style styles))
          [status-bar {:bar-style "light-content"}]
          [img-bg {:source (get-image-for-weather @weather-state-name)
                   :style (:image-container styles)
                   :image-style (:image styles)}
           [view {:style (:details-container styles)}
            ; (println "view update")
            ; (j/call js/console :log "City name: " @city-name)
            [activity-indicator {:animating @loading? :color "black" :size "large"}]
            (when-not @loading?
              (if-not @error?
                [weather-info @city-name @weather-state-name @temperature]
                [text {:style (merge (:small-text styles) (:text-style styles))} "Could not load weather, please try a different city."]))
              ; [view
               ; [text {:style (merge (:large-text styles) (:text-style styles))} "Loading!!!!!!!!!!!!!!!!"

            [search-input {:placeholder "Search any city" :on-submit handle-update-location}]
            ; (j/call js/console :log "Weather state name: " @weather-state-name)
            (j/call js/console :log "APP-DB: " (clj->js @app-db))]]])})))
            ; (j/call js/console :log "image name keyword: " (keyword (string/replace "Heavy Rain" " " "")))]]])})))

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch-sync [:initialize-weather-info])
  ; (dispatch-sync [:fetch-location-id "Chittagong"])
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
