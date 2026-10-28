package listas;
 
import java.util.NoSuchElementException;
import java.util.Objects;
 
/**
 * Lista simplemente enlazada genérica.
 * Solo usa nodos propios (clase Nodo), sin colecciones de java.util.
 */
public class ListaEnlazada<T> {
 
    /** Primer nodo de la lista (null cuando la lista está vacía). */
    private Nodo<T> cabeza;
 
    /** Cantidad de elementos. Se mantiene actualizada en cada operación. */
    private int cantidad;
 
    public ListaEnlazada() {
        this.cabeza = null;
        this.cantidad = 0;
    }
 
    // =====================================================================
    // ETAPA 1. Estado de la lista
    // =====================================================================
 
    /** @return true si la lista no tiene elementos. */
    public boolean estaVacia() {
        return cantidad == 0;
    }
 
    /** @return número de elementos de la lista. */
    public int tamanio() {
        return cantidad;
    }
 
    // =====================================================================
    // ETAPA 2. Agregar en los extremos
    // =====================================================================
 
    /** Inserta el dato como nuevo primer elemento. */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        cantidad++;
    }
 
    /** Inserta el dato como nuevo último elemento. */
    public void agregarAlFinal(T dato) {
        if (estaVacia()) {
            agregarAlInicio(dato);
            return;
        }
        Nodo<T> ultimo = nodoEn(cantidad - 1);
        ultimo.setSiguiente(new Nodo<>(dato));
        cantidad++;
    }
 
    // =====================================================================
    // ETAPA 3. Consultar elementos
    // =====================================================================
 
    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerPrimero() {
        validarNoVacia();
        return cabeza.getDato();
    }
 
    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerUltimo() {
        validarNoVacia();
        return nodoEn(cantidad - 1).getDato();
    }
 
    /**
     * @param indice posición (0 = primer elemento)
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T obtener(int indice) {
        validarIndiceElemento(indice);
        return nodoEn(indice).getDato();
    }
 
    // =====================================================================
    // ETAPA 4. Búsqueda
    // =====================================================================
 
    /** @return true si algún elemento es igual (equals) al dato. Admite null. */
    public boolean contiene(T dato) {
        return indiceDe(dato) != -1;
    }
 
    /** @return posición de la primera aparición del dato, o -1 si no está. */
    public int indiceDe(T dato) {
        Nodo<T> actual = cabeza;
        int posicion = 0;
        while (actual != null) {
            if (Objects.equals(actual.getDato(), dato)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return -1;
    }
 
    // =====================================================================
    // ETAPA 5. Insertar y reemplazar en cualquier posición
    // =====================================================================
 
    /**
     * Inserta el dato en la posición indicada, desplazando los siguientes.
     * Se permite indice == tamanio() (equivale a agregar al final).
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice > tamanio()
     */
    public void insertar(int indice, T dato) {
        if (indice < 0 || indice > cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        if (indice == 0) {
            agregarAlInicio(dato);
            return;
        }
        Nodo<T> anterior = nodoEn(indice - 1);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(anterior.getSiguiente());
        anterior.setSiguiente(nuevo);
        cantidad++;
    }
 
    /**
     * Reemplaza el dato de la posición indicada.
     *
     * @return el dato que había antes
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T reemplazar(int indice, T dato) {
        validarIndiceElemento(indice);
        Nodo<T> nodo = nodoEn(indice);
        T anterior = nodo.getDato();
        nodo.setDato(dato);
        return anterior;
    }
 
    // =====================================================================
    // ETAPA 6. Eliminar
    // =====================================================================
 
    /**
     * Elimina y retorna el primer elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarPrimero() {
        validarNoVacia();
        T dato = cabeza.getDato();
        cabeza = cabeza.getSiguiente();
        cantidad--;
        return dato;
    }
 
    /**
     * Elimina y retorna el último elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarUltimo() {
        validarNoVacia();
        if (cantidad == 1) {
            return eliminarPrimero();
        }
        Nodo<T> penultimo = nodoEn(cantidad - 2);
        T dato = penultimo.getSiguiente().getDato();
        penultimo.setSiguiente(null);
        cantidad--;
        return dato;
    }
 
    /**
     * Elimina y retorna el elemento de la posición indicada.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T eliminar(int indice) {
        validarIndiceElemento(indice);
        if (indice == 0) {
            return eliminarPrimero();
        }
        Nodo<T> anterior = nodoEn(indice - 1);
        Nodo<T> aEliminar = anterior.getSiguiente();
        anterior.setSiguiente(aEliminar.getSiguiente());
        cantidad--;
        return aEliminar.getDato();
    }
 
    /**
     * Elimina la primera aparición del dato.
     * @return true si se eliminó algo, false si el dato no estaba
     */
    public boolean eliminarValor(T dato) {
        int indice = indiceDe(dato);
        if (indice == -1) {
            return false;
        }
        eliminar(indice);
        return true;
    }
 
    /** Deja la lista vacía. */
    public void vaciar() {
        cabeza = null;
        cantidad = 0;
    }
 
    // =====================================================================
    // ETAPA 7. Representación en texto
    // =====================================================================
 
    /**
     * Formato: "[1, 2, 3]". Lista vacía: "[]".
     * Un elemento null se imprime como "null".
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        return sb.append("]").toString();
    }
 
    // =====================================================================
    // ETAPA 8. Retos
    // =====================================================================
 
    /**
     * Invierte la lista EN SITIO: no crea nodos nuevos, solo cambia
     * las referencias 'siguiente' de los nodos existentes.
     */
    public void invertir() {
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            Nodo<T> siguiente = actual.getSiguiente();
            actual.setSiguiente(anterior);
            anterior = actual;
            actual = siguiente;
        }
        cabeza = anterior;
    }
 
    /**
     * Retorna el elemento del medio. Si el tamaño es par, el segundo de los
     * dos centrales (ej.: [1,2,3,4] -> 3).
     *
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerMedio() {
        validarNoVacia();
        Nodo<T> lento = cabeza;
        Nodo<T> rapido = cabeza;
        while (rapido != null && rapido.getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }
        return lento.getDato();
    }
 
    /**
     * Elimina los elementos repetidos dejando solo la primera aparición
     * de cada uno, conservando el orden. Ej.: [1,2,1,3,2] -> [1,2,3]
     */
    public void eliminarDuplicados() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            // Se eliminan del resto de la lista los nodos iguales al actual
            Nodo<T> previo = actual;
            Nodo<T> revisado = actual.getSiguiente();
            while (revisado != null) {
                if (Objects.equals(actual.getDato(), revisado.getDato())) {
                    previo.setSiguiente(revisado.getSiguiente());
                    cantidad--;
                } else {
                    previo = revisado;
                }
                revisado = revisado.getSiguiente();
            }
            actual = actual.getSiguiente();
        }
    }
 
    // =====================================================================
    // Métodos auxiliares privados
    // =====================================================================
 
    /** Lanza excepción si la lista está vacía. */
    private void validarNoVacia() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
    }
 
    /** Valida que el índice apunte a un elemento existente. */
    private void validarIndiceElemento(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }
 
    /** Retorna el nodo de la posición indicada (se asume índice válido). */
    private Nodo<T> nodoEn(int indice) {
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }
}