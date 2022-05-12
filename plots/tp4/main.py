import pandas
import numpy as np

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
    plots.plot_mse(lists[0], lists[1], lists[2], lists[3])


def plots_system2():
    # plots_2_1()
    # plots_2_2()
    plots_2_3()
    # plots_2_4()


def plots_2_1():
    col_names = []
    lists = read_csv_columns_to_lists(sys2_path + ".csv", col_names)


def plots_2_2():
    col_names = []
    lists = read_csv_columns_to_lists(sys2_path + ".csv", col_names)


def plots_2_3():
    col_names = ['v0', 'left', 'right', 'up', 'down', 'absorb']
    lists = read_csv_columns_to_lists(sys2_path + "percentages-v0.csv", col_names)
    plots.percentages_per_v0(np.array(lists[0]), np.array(lists[1]), np.array(lists[2]),
                             np.array(lists[3]), np.array(lists[4]), np.array(lists[5]))

    # fake data
    v0 = np.array([5e3, 1e4, 5e4])
    left = np.array([0.4, 0.2, 0.1])
    right = np.array([0.1, 0.2, 0.5])
    up = np.array([0.05, 0, 0.05])
    down = np.array([0, 0, 0.05])
    absorb = np.array([0.45, 0.6, 0.3])

    plots.percentages_per_v0(v0, left * 100, right * 100, up * 100, down * 100, absorb * 100)


def plots_2_4():
    col_names = []
    lists = read_csv_columns_to_lists(sys2_path + ".csv", col_names)


def read_csv_columns_to_lists(file_name, col_names):
    data = pandas.read_csv(file_name)
    lists = []

    # converting column data to list
    for name in col_names:
        lists.append(data[name].tolist())

    return lists


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    plots_system1()
    # plots_system2()
