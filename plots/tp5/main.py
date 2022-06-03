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

    plots.time_vs_sth(Ds, times, flows, 'Caudal [partículas/s]', 'Evolución temporal del caudal',
                      f'Sliding window = {sliding_window}')
    plots.mean_and_stdev(Ds, mean_list, stdev_list)


def ej2():
    print("Plots ejercicio 2...")
    ej_path = tp5_path + "ej2/"


def ej3():
    print("Plots ejercicio 3...")
    ej_path = tp5_path + "ej3/"


def ej4():
    print("Plots ejercicio 4...")
    ej_path = tp5_path + "ej4/"


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    ej1()
    ej2()
    ej3()
    ej4()
