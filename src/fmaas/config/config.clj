(ns fmaas.config.config
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [ring.util.response :as response]))

(def data-readers {'fmaas/songs-files #(file-seq (io/file %1))
                   'fmaas/songs-resources #(file-seq (io/resource %1))
                   'fmaas/song-file io/file
                   'fmaas/song-resource io/resource})

(def config (atom {}))

(defn set-config
  ([] (set-config (io/resource (env :config-file))))
  ([resource-or-file]
    (with-open [rdr (java.io.PushbackReader. (io/reader resource-or-file))]
      (swap! config (fn [n] (edn/read {:readers data-readers} rdr))))))

(defn get-config []
  (if (empty? @config)
    (set-config))
  @config)

(defn get-config-handler [req]
  (response/response (get-config)))

(defn put-config-handler [req])
