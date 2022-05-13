import pandas
import numpy as np
import seaborn as sns
from matplotlib import pyplot as plt

import plots

tp4_path = "../../TP4/outputs/"
sys2_path = tp4_path + "system2/"


def plots_system1():
    sys1_path = tp4_path + "system1/"

    col_names = ['t', 'beeman', 'verlet', 'gpc', 'analytical_solution']
    lists = read_csv_columns_to_lists(sys1_path + "positions.csv", col_names)
    plots.plot_solutions(lists[0], lists[1], lists[2], lists[3], lists[4])
    # TODO: zoom in

    col_names = ['dt', 'beeman', 'verlet', 'gpc']
    lists = read_csv_columns_to_lists(sys1_path + "dt-mse.csv", col_names)
    # plots.plot_mse(lists[0], lists[1], lists[2], lists[3])


def plots_system2():
    # plots_2_1()
    # plots_2_2()
    # plots_2_3()
    plots_2_4()


def plots_2_1():
    dts = [1e-18, 1e-17, 1e-16, 1e-15, 1e-14]
    col_names = ['t', ' energy_diff']

    times = []
    reds = []   # relative energy differences

    for dt in [18, 17, 16, 15, 14]:
        lists = read_csv_columns_to_lists(sys2_path + "dieron_mejor/energydiff-dt-1.0E-" + str(dt) + ".csv", col_names)
        times.append(lists[0])
        reds.append(lists[1])

    plots.time_vs_energy(dts, times, reds, 'diferencia relativa de energía', 5e4)

    red_avgs = []   # relative energy differences averages
    stdevs = []

    for red in reds:
        stdevs.append(np.std(red))
        red_avgs.append(sum(red) / len(red))

    plots.dt_vs_energy(dts, red_avgs, stdevs, 'promedio diferencia relativa de energía', 5e4)


def plots_2_2():
    col_names = ['v_module', ' average_length', ' std_length']
    lists = read_csv_columns_to_lists(sys2_path + "lengthtrajectory.csv", col_names)
    plots.v0_vs_len(lists[0], lists[1], lists[2], 'longitud promedio de la trayectoria', 1e-15)

    col_names = ['x', ' y']
    v0s = [5000, 15000, 25000, 35000, 45000]

    for v0 in v0s:
        lists = read_csv_columns_to_lists(sys2_path + "positions-vo-" + str(v0) + ".0.csv", col_names)
        plots.trajectory(lists[0], lists[1], v0, 1e-15)


def plots_2_3():
    col_names = ['v0', 'left', 'right', 'up', 'down', 'absorb']
    lists = read_csv_columns_to_lists(sys2_path + "endState.csv", col_names)
    plots.percentages_per_v0(np.array(lists[0]), np.array(lists[1])*100, np.array(lists[2])*100,
                             np.array(lists[3])*100, np.array(lists[4])*100, np.array(lists[5])*100, 1e-15)


def plots_2_4():
    col_names = ['trajectory']
    v0s = [5000, 15000, 25000, 35000, 45000]
    data_list = []

    for v0 in v0s:
        data_list.append(read_csv_columns_to_lists(sys2_path + "absorbed-trajectory-vo-" + str(v0) + ".0.csv", col_names))

    plots.trajectory_len_pdf(v0s, data_list)  # probability density function


def read_csv_columns_to_lists(file_name, col_names):
    data = pandas.read_csv(file_name)
    lists = []

    # converting column data to list
    for name in col_names:
        lists.append(data[name].tolist())

    return lists


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    # plots_system1()
    plots_system2()
