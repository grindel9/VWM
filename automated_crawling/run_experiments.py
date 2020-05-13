import json
import subprocess
import matplotlib.pyplot as plt
import pandas as pd

# Set plot params
plt.rcParams['figure.figsize'] = (12, 8)
plt.rcParams['font.size'] = 12


JAR_FILE_PATH = '/home/gorq/Desktop/VWM/walker_2.0/out/artifacts/walker_2_0_jar/walker_2.0.jar'
DB_URL = 'localhost:3306'
DB_NAME = 'market'
USERNAME = 'gorq'
PASSWORD = 'hesloheslo'

class Experiment:
    def __init__(self, dct: dict) -> None:
        self._dct = dct

    @property
    def timeout(self) -> int:
        return self._dct['timeout']

    @property
    def request_limit(self) -> int:
        return self._dct['request limit']

    @property
    def ordering(self) -> str:
        return self._dct['ordering']

    @property
    def average_depth(self) -> float:
        return self._dct['average depth']

    @property
    def average_branching(self) -> float:
        return self._dct['average branching']

    @property
    def C(self) -> float:
        return self._dct['walker']['C']

    @property
    def acquired_amount(self) -> int:
        return self._dct['walker']['acquired amount']

    @property
    def unaccepted_amount(self) -> int:
        return self._dct['not accepted']

    @property
    def report_frequency(self) -> float:
        return self._dct['reporter']['frequency']

    @property
    def progress_list(self) -> list:
        return self._dct['reporter']['acquired times']

    def toPSV(self) -> str:
        return f'\
{self.timeout}|\
{self.request_limit}|\
{self.ordering}|\
{self.C}|\
{self.average_depth}|\
{self.average_branching}|\
{self.report_frequency}|\
{self.progress_list}|\
{self.acquired_amount}|\
{self.unaccepted_amount}\
'

    def graph(self) -> None:
        plt.rcParams['figure.figsize'] = (12, 7)
        plt.rcParams['font.size'] = 13
        plt.ylabel('Acquired amount')
        plt.xlabel('Time passed (seconds)')

        plt.grid(True)

        plt.plot(
            [0]+[ self.report_frequency*x for x in range(1, len(self.frequency_list)+1)],
            [0]+self.frequency_list,
            label='Progress'
        )


        plt.legend()
        plt.show()


    def outputInfo(self) -> None:
        print(self)
        self.graph()

    def __str__(self) -> str:
        return f'''
--------------------------------------------------------
| Timeout: {self.timeout} seconds
| Request limit: {self.request_limit}
| Ordering: {self.ordering}
| C: {self.C}
| Average depth: {self.average_depth}
| Average branching: {self.average_branching}
| Report frequency: {round(1/self.report_frequency, 3)} updates per second
| Progress list sample: {self.progress_list[:5]}...
| Acquired amount: {self.acquired_amount}
| Not accepted amount: {self.unaccepted_amount}
--------------------------------------------------------
'''

def run_experiment(
        db_url: str,
        db_name: str,
        username: str,
        password: str,
        *_,
        timeout: int = 10,
        request_limit: int = 1,
        C: int = 3,
        frequency: float = 1,
        file_out: str,
        ordering: str = 'fixed',
        wanted: int
    ) -> Experiment:
    to_run = f'''
        java -jar {JAR_FILE_PATH}
        --db-url {db_url}
        --db-name {db_name}
        --username {username}
        --password {password}
        --timeout {timeout}
        --request-limit {request_limit}
        --wanted-sample {wanted}
        --set-c {C}
        --report-frequency {frequency}
        --output {file_out}
        --ordering {ordering}
    '''

    process = subprocess.Popen(to_run.split(), stdout=subprocess.PIPE)
    o, e = process.communicate()

    with open(file_out, 'r') as f:
        return Experiment(json.load(f))


def save_experiment(exper: Experiment, file_name: str) -> None:
    with open(file_name, 'a') as f:
        f.write(exper.toPSV() + '\n')


def run_experiments(
        *_,
        timeouts: list,
        C_s: list,
        limits: list,
        orderings: list,
        output_csv: str,
        num_of_reports: int,
        wanted_sample: int,
        run_times: int
    ) -> None:


    for _ in range(run_times):
        for _timeout in timeouts:
            for _C in C_s:
                for _limit in limits:
                    for _ordering in orderings:
                        print(f'{_timeout}:{_C}:{_limit}:{_ordering}')
                        frequency = _timeout/num_of_reports
                        my_exp = run_experiment(
                            DB_URL,
                            DB_NAME,
                            USERNAME,
                            PASSWORD,
                            file_out = 'dummy.json',
                            timeout = _timeout,
                            C = _C,
                            frequency = frequency,
                            request_limit = _limit,
                            ordering = _ordering,
                            wanted = wanted_sample,
                        )
                        save_experiment(my_exp, output_csv)

    print('Finished!!!')

csv_out = 'experiment_out_10k.csv'

with open(csv_out, 'w') as f:
    f.write('\
Timeout|\
Limit|\
Ordering|\
C|\
Average depth|\
Average branching|\
Report frequency|\
Progress list|\
Acquired amount|\
Unaccepted amount' + '\n')

run_times = 1
# Define max records to stop computations early
# (this is just convenience so that the runs can stop early and we can do more simulations)
DB_RECORDS = 10000
timeouts = [180]
C_s = [0, 1, 2, 3, 4, 5, 6, 7, 8]
limits = [1, 10, 50, 200]
orderings = ['random', 'fixed']
num_of_reports = 100


run_experiments(
    timeouts = timeouts,
    C_s = C_s,
    limits = limits,
    orderings= orderings,
    output_csv = csv_out,
    num_of_reports = num_of_reports,
    wanted_sample = DB_RECORDS,
    run_times = run_times
)
