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

    plots.time_vs_sth(Ds, times, flows, 'Caudal [partículas/s]', 'Evolución temporal del caudal', 'Apertura D [m]')
    plots.mean_and_stdev(Ds, mean_list, stdev_list, 'Valor medio y desviación estándar del caudal')

    return mean_list, stdev_list


def ej2():
    print("Plots ejercicio 2...")
    ej_path = tp5_path + "ej2/"

    beverloo_flows = []

    for i in range(len(Ds)):
        mean = mean_list[i]
        Q, c, error = beverloo(Ds[i], mean)     # lo mas logico seria que c dé entre 0.5 y 2
        beverloo_flows.append(Q)
        print(f'D = {Ds[i]}\n'
              f'mean = {mean:.3f}, Q = {Q:.3f}, c = {c}, relative error = {error / mean:.3f}, '
              f'percentage error = {error / mean * 100:.3f} %\n')

    plots.mean_and_stdev(Ds, mean_list, stdev_list, 'Ajuste del parámetro libre de la ley de Beverloo',
                         beverloo=True, beveerloo_flows=beverloo_flows)


def beverloo(d, mean):
    n = 10 * 33                 # FIXME: chequear # nro de particulas que entran en el silo
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
    col_names = ['time', ' energy']

    times = []
    energies = []

    for D in Ds:
        lists = read_csv_columns_to_lists(f'{ej_path}energies-{str(D)}.csv', col_names)
        times.append(lists[0])
        energies.append(lists[1])

    plots.time_vs_sth(Ds, times, energies, 'Energía [J]', 'Evolución temporal de la energía cinética',
                      'Apertura D [m]', log_scale=True)
    # plots.time_vs_sth(Ds, times, energies, 'Energía [J]', 'Evolución temporal de la energía cinética',
    #                   'Apertura D [m]', log_scale=True, zoom=True)


def ej4():
    print("Plots ejercicio 4...")
    ej_path = tp5_path + "ej4/"
    col_names = ['time', ' energy']

    kt_list = [200000, 400000, 600000]
    times = []
    energies = []

    for kt in kt_list:
        lists = read_csv_columns_to_lists(f'{ej_path}energies-0.0-{str(kt)}.0.csv', col_names)
        times.append(lists[0])
        energies.append(lists[1])

    # TODO: energia residual?

    plots.time_vs_sth(kt_list, times, energies, 'Energía [J]', 'Evolución temporal de la energía cinética',
                      'Parametro de fricción [N/m]', suptitle=f'D = 0')
    plots.residual_energy_vs_friction_param()


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    Ds = [0.15, 0.18, 0.22, 0.25]

    mean_list, stdev_list = ej1()
    ej2()
    # ej3()
    # ej4()
