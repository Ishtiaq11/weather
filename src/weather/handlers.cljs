(ns weather.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor dispatch reg-event-fx]]
    [clojure.spec.alpha :as s]
    [applied-science.js-interop :as j]
    [ajax.core :as ajax :refer [GET]]
    [weather.db :as db :refer [app-db]]))

;; -- Interceptors ----------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/develop/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (->interceptor
        :id :validate-spec
        :after (fn [context]
                 (let [db (-> context :effects :db)]
                   (check-and-throw ::db/app-db db)
                   context)))
    ->interceptor))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
  :initialize-db
  [validate-spec]
  (fn [_ _]
    app-db))

(reg-event-db
  :set-greeting
  [validate-spec]
  (fn [db [_ value]]
    (assoc db :greeting value)))

(reg-event-db
  :set-initial-city
  (fn [db [_ city-name]]
    (assoc db :city-name city-name)))

(reg-event-db
  :update-city-name
  (fn [db [_ new-city-name]]
    (j/call js/console :log "city-name updated to: " new-city-name)
    (assoc db :city-name new-city-name)))

; (reg-event-db
;   :fetch-weather
;   (fn [db [_ woeid]]
;     (let [uri (str "https://www.metaweather.com/api/location/" woeid "/")
;           handler (fn [json-response]
;                     (let [{:keys [title consolidated_weather]} json-response
;                           {:keys [weather_state_name the_temp]} (get consolidated_weather 0)]
;                       ; (j/call js/console :log "the response " json-response)
;                       (j/call js/console :log "location: " title)
;                       (j/call js/console :log "weather: " weather_state_name)
;                       (j/call js/console :log "temperature: " the_temp)))
;                       ; (j/call js/console :log "consolidated_weather: " (clj->js consolidated_weather))))
;           error-handler (fn [{:as args
;                               :keys [status status-text]}]
;                           (j/call js/console :log (str "something bad happened: " status " " status-text)))]
;                           ; (j/call js/console :log (str "args of erro-handler: " args)))]
;       (GET
;         uri
;         {:handler handler
;          :error-handler error-handler
;          :response-format :json
;          :keywords? true})
;       (assoc db :city-name title))))

; (reg-event-db
;   :fetch-location-id
;   (fn [db [_ city]]
;     (let [uri "https://www.metaweather.com/api/location/search/"
;           handler (fn [json-response]
;                     (let [location (get json-response 0)
;                           woeid (:woeid location)]
;                       (j/call js/console :log (str "AJAX GET JSON RESPONSE: " json-response))
;                       (j/call js/console :log (str "LOCATION: " location))
;                       (j/call js/console :log (str "LOCATION WOEID: " woeid))
;                       (dispatch [:fetch-weather woeid])))
;
;           error-handler (fn [{:as args
;                               :keys [status status-text]}]
;                           (j/call js/console :log (str "something bad happened: " status " " status-text)))]
;       (GET
;         uri
;         {:url-params {:query city}
;          :handler handler
;          :error-handler error-handler
;          :response-format :json
;          :keywords? true})
;       (println "fetch data for city" city))))

(reg-event-db
  :get-location-woeid
  (fn [db [_ result]]
    (j/call js/console :log (str "LOCATION" (get result 0)))))

(reg-event-db
  :show-error-message
  (fn [db [_ result]]
    (j/call js/console :error (str "Something bad happened: " (:status result) " " (:status-text result)))))

(reg-event-fx
  :fetch-location-id
  (fn [{:keys [db]} [_ city]]
    {:http-xhrio {:method :get
                  :uri "https://www.metaweather.com/api/location/search/"
                  :params {:query city}
                  :timeout 8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:get-location-woeid]
                  :on-failure [:show-error-message]}}))
