# Impede que o otimizador apague seus métodos de teste
-keepclasseswithmembers class * {
    @org.junit.Test <methods>;
}

# Impede que o otimizador apague a regra do benchmark
-keepclasseswithmembers class * {
    @androidx.benchmark.junit4.BenchmarkRule <fields>;
}

# Desativa a ofuscação de nomes para que o console exiba os resultados legíveis
-dontobfuscate