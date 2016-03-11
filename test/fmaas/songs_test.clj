(ns fmaas.songs-test
  (:require [clojure.test :refer :all]
            [fmaas.songs :refer :all]))

; TODO: Figure out how to test the other part of this or clause?
(deftest test-songs-dir
  (testing "FMAAS_HOME unset defaults to $HOME/songs"
    (is (= songs-dir (str (System/getenv "HOME") "/songs")))))

(deftest test-get-song-name
  (testing "Calls getName on a File and removes '.mid'"
    (is (= (get-song-name (clojure.java.io/file "/test.mid")) "test"))))

(deftest test-get-song-files
  (testing "No songs directory returns an empty array"
    (is (empty? (get-song-files))))
  (testing "something"
    (with-redefs-fn {#'fmaas.songs/songs-dir (fn [] "/fizz")}
      #(is (= (songs-dir) "/fizz")))))


; TODO: Setup / Teardown of a tmpdir? Mocking?
