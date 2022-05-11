import pandas

import plots

tp4_path = "../../TP4/outputs/"


def plots_system1():
    sys1_path = tp4_path + "system1/"

    data = pandas.read_csv(sys1_path + "positions.csv")

    # converting column data to list
    time = data['t'].tolist()
    beeman = data['beeman'].tolist()
    verlet = data['verlet'].tolist()
    gpc = data['gpc'].tolist()
    analytical_solution = data['analytical_solution'].tolist()

    plots.plot_solutions(time, beeman, verlet, gpc, analytical_solution)

    data = pandas.read_csv(sys1_path + "dt-mse.csv")

    # converting column data to list
    dt = data['dt'].tolist()
    beeman = data['beeman'].tolist()
    verlet = data['verlet'].tolist()
    gpc = data['gpc'].tolist()

    plots.plot_mse(dt, beeman, verlet, gpc)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    plots_system1()
