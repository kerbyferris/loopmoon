(ns loopmoon.core
  (:use overtone.live))

(stop)

(demo 10 (sin-osc (+ 1000 (* 600 (lf-noise0:kr 12))) 0.3))
(demo 30 (rlpf (dust [12 15]) (+ 1600 (* 1500 (lf-noise1 [1/3, 1/4]))) 0.02 ))
(demo 30 (let [sines 5
              speed 6]
          (* (mix
               (map #(pan2 (* (sin-osc (* % 100))
                              (max 0 (+ (lf-noise1:kr speed) (line:kr 1 -1 30))))
                           (- (clojure.core/rand 2) 1))
                    (range sines)))
             (/ 1 sines))))


