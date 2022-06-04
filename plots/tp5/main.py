import pandas
import numpy as np

import plots

tp5_path = "../../TP5/outputs/"


def read_csv_columns_to_lists(file_name, col_names):
    data = pandas.read_csv(file_name)
    lists = []

    # converting column data to list
    for name in col_names:
        lists.append(data[name].tolist())

    return lists


def ej1():
    print("Plots ejercicio 1...")
    ej_path = tp5_path + "ej1/"
    Ds = [0.15, 0.18, 0.22, 0.25]
    col_names = ['time', ' caudal']

    times = []
    flows = []
    mean_list = []
    stdev_list = []

    for D in Ds:
        lists = read_csv_columns_to_lists(f'{ej_path}caudal-{str(D)}.csv', col_names)
        times.append(lists[0])
        flow = lists[1]
        flows.append(flow)
        mean_list.append(np.mean(flow))
        stdev_list.append(np.std(flow))

    # TODO: sliding window?
    sliding_window = 5

    # plots.time_vs_sth(Ds, times, flows, 'Caudal [partículas/s]', 'Evolución temporal del caudal',
    #                   f'Sliding window = {sliding_window}')
    # plots.mean_and_stdev(Ds, mean_list, stdev_list)

    return Ds, mean_list, stdev_list


def ej2(Ds, mean_list):
    print("Plots ejercicio 2...")
    ej_path = tp5_path + "ej2/"

    for i in range(len(Ds)):
        Q, c, error = beverloo(Ds[i], mean_list[i])     # lo mas logico seria que c dé entre 0.5 y 2
        print(f'D = {Ds[i]}\n'
              f'mean = {mean_list[i]}, Q = {Q}, c = {c}, error = {error}\n')

    # TODO: plot mean vs beverloo_Q


def beverloo(d, mean):
    n = 10 * 34                 # FIXME: chequear # nro de particulas que entran en el silo
    area = 1 * 0.3              # FIXME: chequear # area del silo
    n_p = n / area              # nro de particulas por unidad de volumen
    g_square_root = 3.13        # raiz de la gravedad
    r = (0.01 + 0.015) / 2      # radio medio de las particulas
    exp = 1.5                   # exponente para silo 2D (para 3D es 2.5)

    min_error = float('inf')
    best_c = 0
    best_Q = 0

    for c in range(50):     # [0, 5]
        c = c / 10          # step 0.1
        Q = n_p * g_square_root * pow(d - c * r, exp)
        error = abs(Q - mean)
        if error < min_error:
            min_error = error
            best_c = c
            best_Q = Q

    return best_Q, best_c, min_error    # lo mas logico seria que c dé entre 0.5 y 2


def ej3():
    print("Plots ejercicio 3...")
    ej_path = tp5_path + "ej3/"

    # plots.time_vs_sth(Ds, times, energies, 'Energía [J]', 'Evolución temporal de la energía cinética',
    #                   f'Sliding window = {sliding_window}')


def ej4():
    print("Plots ejercicio 4...")
    ej_path = tp5_path + "ej4/"

    # plots.time_vs_sth([0], times, energies, 'Energía [J]', 'Evolución temporal de la energía cinética',
    #                   f'Sliding window = {sliding_window}, D = 0')
    # plots.residual_energy_vs_friction_param()


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    Ds, mean_list, stdev_list = ej1()
    ej2(Ds, mean_list)
    ej3()
    ej4()
